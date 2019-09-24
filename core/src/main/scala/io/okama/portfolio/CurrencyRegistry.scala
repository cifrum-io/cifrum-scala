package io.okama
package portfolio

import unit._
import source._
import timeseries._

import javax.inject._

class CurrencyRegistry @Inject(
  cbrCurrencyRatesSource: CbrCurrencyRatesSource,
) {

  def convert[T <: PeriodFrequency](series: TimeSeries[T, Double], from: Currency, to: Currency): TimeSeries[T, Double] = 
    (from, to) match {
      case (f, t) if f.equals(t) =>
        series

      case (f, t) =>
        val rate = series.map(x => 1.0)
        Math.mul(series, rate)
    }

}
