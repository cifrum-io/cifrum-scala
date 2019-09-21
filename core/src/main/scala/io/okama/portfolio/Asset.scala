package io.okama
package portfolio

import unit._
import source._
import timeseries._

import javax.inject._

class Asset(symbol: FinancialSymbol) {

  def closeValues[T <: PeriodFrequency](
    slice: Slice[T], currency: Currency
  ) given (periodFrequency: T): TimeSeriesResult[T] = {
    val cv = symbol.closeValues
    val result: TimeSeriesResult[T] = cv.as(periodFrequency)
    result
  }

  override def toString(): String = {
    s"Asset(symobl=$symbol)"
  }

}

class Registry @Inject(
  usDataSource: UsDataSource,
  micexStockDataSource: MicexStockDataSource,
) () {
  def get(code: String): Option[Asset] = {
    val Array(namespace, cod) = code.split("/")
    namespace match {
      case "us" =>
        val symOpt = usDataSource.getFinancialSymbol(code=cod)
        symOpt.map { sym => Asset(symbol=sym) }
      case "micex" =>
        val symOpt = micexStockDataSource.getFinancialSymbol(code=cod)
        symOpt.map { sym => Asset(symbol=sym) }
      case _ =>
        None
    }
  }
}
