package io.cifrum.grpc

import java.util.logging.Logger

import io.cifrum.protos.hello.{GreeterGrpc, HelloReply, HelloRequest}
import io.grpc.{Server, ServerBuilder}

import scala.concurrent.{ExecutionContext, Future}

trait Service {
  def sayHello(msg: String): String
}

object HelloWorldServer {
  private val logger = Logger.getLogger(classOf[HelloWorldServer].getName)
}

class HelloWorldServer(service: Service, executionContext: ExecutionContext) {
  self =>

  private[this] var server: Server = _

  def start(host: String, port: Int): Unit = {
    server = ServerBuilder.forPort(port).addService(GreeterGrpc.bindService(new GreeterImpl, executionContext)).build.start
    HelloWorldServer.logger.info("Server started, listening on " + port)
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
