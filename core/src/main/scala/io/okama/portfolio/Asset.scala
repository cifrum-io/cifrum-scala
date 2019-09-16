package io.okama
package portfolio

import unit._
import source._
import timeseries._

import javax.inject._

class Asset(symbol: FinancialSymbol) {

  def closeValues[T <: PeriodFrequency](slice: Slice[T], currency: Currency): Slice[T]#ResultType = {
    symbol.closeValues
  }

  override def toString(): String = {
    s"Asset(symobl=$symbol)"
  }

}

class Registry @Inject(usDataSource: UsDataSource) () {
  def get(code: String): Option[Asset] = {
    val Array(namespace, cod) = code.split("/")
    namespace match {
      case "us" =>
        val symOpt = usDataSource.getFinancialSymbol(code=cod)
        symOpt.map { sym => Asset(symbol=sym) }
      case _ =>
        None
    }
  }
}
