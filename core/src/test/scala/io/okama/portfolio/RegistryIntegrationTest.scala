package io.okama
package portfolio

import com.google.inject.{Guice, AbstractModule}
import portfolio.{AssetNamespace => ANS}
import verify._

object RegistryIntegrationTest extends BasicTestSuite with Common {
  test("Registry.get") {
    val registry = injector.getInstance(classOf[portfolio.Registry])
    assert((registry.get(ANS.micex, "SBER"): Option[AssetMICEX]).isDefined)
    assert((registry.get(ANS.us, "AAPL"): Option[AssetUS]).isDefined)
    assert((registry.get(ANS.us, "absent"): Option[AssetUS]).isEmpty)
  }
}
