package io.cifrum
package bond

import java.util.concurrent.TimeUnit

import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import io.cifrum.protos.hello.{GreeterGrpc, HelloReply, HelloRequest}
import verify._
import grpc.{HelloWorldServer, ServiceImpl}

import scala.concurrent.ExecutionContext

object BondTest extends BasicTestSuite {
  test("exists") {
    val bondMeta = new BondMeta(Vector(
      BondInfo("isin1", "name1"),
      BondInfo("isin2", "name2")
    ))

    assert(bondMeta.find("isin1").isDefined)
    assert(bondMeta.find("isin_absent").isEmpty)
  }
}
