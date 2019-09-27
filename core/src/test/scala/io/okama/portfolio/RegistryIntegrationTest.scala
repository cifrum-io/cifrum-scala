package io.okama
package portfolio

import com.google.inject.{Guice, AbstractModule}
import portfolio.{AssetNamespace => ANS}
import verify._

class Module extends AbstractModule

object RegistryIntegrationTest extends BasicTestSuite {
  val injector = Guice.createInjector(Module())

  test("Registry.get") {
    val registry = injector.getInstance(classOf[portfolio.Registry])
    assert((registry.get(ANS.micex, "SBER"): Option[AssetMICEX]).isDefined)
    assert((registry.get(ANS.us, "AAPL"): Option[AssetUS]).isDefined)
    assert((registry.get(ANS.us, "absent"): Option[AssetUS]).isEmpty)
  }
}
