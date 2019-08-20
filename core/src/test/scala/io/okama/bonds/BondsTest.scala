package io.okama.bonds

import verify._

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
