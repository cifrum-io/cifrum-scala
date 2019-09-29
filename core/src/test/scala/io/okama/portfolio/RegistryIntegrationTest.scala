package io.okama
package portfolio

import portfolio.{AssetNamespace => ANS}
import verify._

object RegistryIntegrationTest extends BasicTestSuite with Common {
  test("Registry.get") {
    assert((registry.get(ANS.micex, "SBER"): Option[AssetMICEX]).isDefined)
    assert((registry.get(ANS.us, "AAPL"): Option[AssetUS]).isDefined)
    assert((registry.get(ANS.us, "absent"): Option[AssetUS]).isEmpty)
  }
}
