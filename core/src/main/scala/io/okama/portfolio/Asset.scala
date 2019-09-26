package io.okama
package portfolio

import unit._
import source._
import timeseries._

import javax.inject._

class Asset[T <: PeriodFrequency, TS <: TimeSeries[T, Double], FT <: FinancialSymbol[T, TS]](
  symbol: FT, currencyRegistry: CurrencyRegistry
) {

  def closeValues[V <: PeriodFrequency](
    slice: Slice[V], currency: Currency
  ) (given periodFrequency: V): TimeSeriesResult[V] = {
    val cvToCurrency = currencyRegistry.convert(series=symbol.closeValues, from=symbol.currency, to=currency)
    cvToCurrency.as(periodFrequency)
  }

  override def toString(): String = {
    s"Asset(symobl=$symbol)"
  }

}

class Registry @Inject(
  usDataSource: UsDataSource,
  micexStockDataSource: MicexStockDataSource,
  currencyRegistry: CurrencyRegistry,
) {
  def get(code: String): Option[Asset[_, _, _]] = {
    val Array(namespace, cod) = code.split("/")
    namespace match {
      case "us" =>
        val symOpt = usDataSource.getFinancialSymbol(code=cod)
        symOpt.map { sym => 
          Asset[PeriodFrequency.Month, VectorEomSeries[Double], UsFinancialSymbol](symbol=sym, currencyRegistry=currencyRegistry) 
        }
      case "micex" =>
        val symOpt = micexStockDataSource.getFinancialSymbol(code=cod)
        symOpt.map { sym => 
          Asset[PeriodFrequency.Day, VectorEodSeries[Double], MicexStockFinancialSymbol](symbol=sym, currencyRegistry=currencyRegistry) 
        }
      case _ =>
        None
    }
  }
}
