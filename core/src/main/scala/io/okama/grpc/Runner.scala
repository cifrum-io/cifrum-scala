package io.okama.grpc

import scala.concurrent.ExecutionContext

class ServiceImpl extends Service {
  override def sayHello(msg: String): String = {
    "Hello there, " + msg + "!"
  }
}

object GrpcRunner {
  def main(args: Array[String]): Unit = {
    val service = ServiceImpl()
    val server = HelloWorldServer(service, ExecutionContext.global)
    server.start("localhost", 50051)
    server.blockUntilShutdown()
  }
}
