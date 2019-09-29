package io.okama

import com.google.inject.{Guice, AbstractModule}

class Module extends AbstractModule

trait Common {
  val injector = Guice.createInjector(Module())
  val registry = injector.getInstance(classOf[portfolio.Registry])
}
