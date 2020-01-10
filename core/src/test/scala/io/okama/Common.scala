package io.cifrum

import com.google.inject.{Guice, AbstractModule}

class Module extends AbstractModule

class Common {
  val injector = Guice.createInjector(Module())
  val registry = injector.getInstance(classOf[portfolio.Registry])
}
