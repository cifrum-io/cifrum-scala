package io.cifrum
package unit

sealed trait Currency derives Eql {
  val name: String
}

object Currency {
  case class RUB(name: String) extends Currency
  case class USD(name: String) extends Currency
  case class EUR(name: String) extends Currency
  case class CNY(name: String) extends Currency

  val rub = RUB(name="RUB")
  val usd = USD(name="USD")
  val eur = EUR(name="EUR")
  val cny = CNY(name="CNY")

  def valueOf(v: String) = v.toUpperCase match {
    case rub.name => rub
    case usd.name => usd
    case eur.name => eur
    case cny.name => cny
    case _        => ???
  }
}
