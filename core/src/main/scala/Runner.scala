import scala.concurrent.ExecutionContext

class ServiceImpl extends Service {
  override def sayHello(msg: String): String = {
    "Hello there, " + msg + "!"
  }
}

object Runner {
  def main(args: Array[String]): Unit = {
    val service = ServiceImpl()
    val server = HelloWorldServer(service, ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }
}
