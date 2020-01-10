package io.cifrum
package bond

import java.util.concurrent.TimeUnit

import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import io.cifrum.protos.hello.{GreeterGrpc, HelloReply, HelloRequest}
import verify._
import grpc.{HelloWorldServer, ServiceImpl}

import scala.concurrent.ExecutionContext

object BondGrpcTest extends TestSuite[GreeterGrpc.GreeterBlockingStub] {
  val host = "localhost"
  val port = 50051
  var server: HelloWorldServer = _
  var channel: ManagedChannel = _

  def setup(): GreeterGrpc.GreeterBlockingStub = {
    val blockingStub = GreeterGrpc.blockingStub(channel)
    blockingStub
  }

  def tearDown(env: GreeterGrpc.GreeterBlockingStub): Unit = {
  }

  override def setupSuite(): Unit = {
    val service = ServiceImpl()
    server = HelloWorldServer(service, ExecutionContext.global)
    server.start(host, port)
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build
  }

  override def tearDownSuite(): Unit = {
    channel.shutdown()
    channel.awaitTermination(30, TimeUnit.SECONDS)

    server.stop()
    server.blockUntilShutdown()
  }

  test("client") { blockingStub =>
    val name = "World"
    val request = HelloRequest(name = name)

    val reply: HelloReply = blockingStub.sayHello(request)
    assert(reply.message == "Hello there, " + name + "!")
  }
}
