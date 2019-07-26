package io.okama
package bonds

import org.junit.Test
import org.junit.Assert._

class BondsMeta_Test {
  @Test def exists(): Unit = {
    val bondsMeta = new BondsMeta(Vector(
      BondInfo("isin1", "name1"),
      BondInfo("isin2", "name2")
    ))

    assertTrue(bondsMeta.find("isin1").isDefined)
    assertTrue(bondsMeta.find("isin_absent").isEmpty)
  }
}

class Bond_Test {
  @Test def hello(): Unit = {
    assertEquals(2 + 2, 4)
  }
}
