package io.okama
package portfolio

import portfolio.{AssetNamespace => ANS}
import verify._

object RegistryIntegrationTest extends TestSuite[Common] {
  def setup(): Common = {
    val common = Common()
    common
  }

  def tearDown(env: Common): Unit = {
  }

  test("Registry.get") { c =>
    assert((c.registry.get(ANS.micex, "SBER"): Option[AssetMICEX]).isDefined)
    assert((c.registry.get(ANS.us, "AAPL"): Option[AssetUS]).isDefined)
    assert((c.registry.get(ANS.us, "absent"): Option[AssetUS]).isEmpty)
  }
}
