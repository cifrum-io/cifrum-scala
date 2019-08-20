import java.util.logging.Logger

import com.example.protos.hello.{GreeterGrpc, HelloReply, HelloRequest}
import io.grpc.{Server, ServerBuilder}

import scala.concurrent.{ExecutionContext, Future}

trait Service {
  def sayHello(msg: String): String
}

object HelloWorldServer {
  private val logger = Logger.getLogger(classOf[HelloWorldServer].getName)

  private val port = 50051
}

class HelloWorldServer(service: Service, executionContext: ExecutionContext) {
  self =>

  private[this] var server: Server = null

  def start(): Unit = {
    server = ServerBuilder.forPort(HelloWorldServer.port).addService(GreeterGrpc.bindService(new GreeterImpl, executionContext)).build.start
    HelloWorldServer.logger.info("Server started, listening on " + HelloWorldServer.port)

    System.in.read()

    System.err.println("*** shutting down gRPC server since JVM is shutting down")
    self.stop()
    System.err.println("*** server shut down")
  }

  def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def sayHello(req: HelloRequest): Future[HelloReply] = {
      val message = service.sayHello(req.name)
      val reply = HelloReply(message = message)
      Future.successful(reply)
    }
  }

}
