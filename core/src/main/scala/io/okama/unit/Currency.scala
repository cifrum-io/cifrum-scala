package io.okama
package unit

sealed trait Currency derives Eql

object Currency {
  case class RUB() extends Currency
  case class USD() extends Currency
  case class EUR() extends Currency
  case class CNY() extends Currency

  val rub = RUB()
  val usd = USD()
  val eur = EUR()
  val cny = CNY()

  def valueOf(v: String) = v.toLowerCase match {
    case "rub" => rub
    case "usd" => usd
    case "eur" => eur
    case "cny" => cny
    case _     => ???
  }
}
