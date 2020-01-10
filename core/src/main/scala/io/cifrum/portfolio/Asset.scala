package io.cifrum
package portfolio

import unit._
import source._
import timeseries._

import javax.inject._

class Asset[T <: PeriodFrequency, TS <: TimeSeries[T, Double], FT <: FinancialSymbol[T, TS]](
  val symbol: FT, 
  currencyRegistry: CurrencyRegistry
) {

  def closeValues[V <: PeriodFrequency](
    slice: Slice[V], currency: Currency
  ) (given periodFrequency: V): TimeSeries[V, Double] = {
    currencyRegistry.convert(series=symbol.closeValues, from=symbol.currency, to=currency)
                    .filterIndex(d => {
                      (slice.startDate.isBefore(d) || slice.startDate.isEqual(d)) && 
                        (d.isBefore(slice.endDate) || d.isEqual(slice.endDate))
                    })
                    .as(periodFrequency)
  }

  override def toString(): String = {
    s"Asset(symobl=$symbol)"
  }

}
