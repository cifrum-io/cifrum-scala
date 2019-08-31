package io.okama
package bonds

import java.util.concurrent.TimeUnit

import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import io.okama.protos.hello.{GreeterGrpc, HelloReply, HelloRequest}
import verify._
import grpc.{HelloWorldServer, ServiceImpl}

import scala.concurrent.ExecutionContext

object BondsTest extends BasicTestSuite {
  test("exists") {
    val bondsMeta = new BondsMeta(Vector(
      BondInfo("isin1", "name1"),
      BondInfo("isin2", "name2")
    ))

    assert(bondsMeta.find("isin1").isDefined)
    assert(bondsMeta.find("isin_absent").isEmpty)
  }
}
