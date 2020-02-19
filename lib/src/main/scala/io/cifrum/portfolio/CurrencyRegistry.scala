package io.cifrum
package portfolio

import unit._
import source.CbrCurrencyRatesSource
import timeseries.{timeSeriesOps, _}

import javax.inject._

class CurrencyRegistry @Inject(
  cbrCurrencyRatesSource: CbrCurrencyRatesSource,
) {

  def convert[T1 <: PeriodFrequency, T2 <: PeriodFrequency]
      (series: TimeSeries[T1, Double], from: Currency, to: Currency)
      (given periodFrequency: T2): TimeSeries[T2, Double] = {
    (from, to) match {
      case (f, t) if f == t =>
        series.as(periodFrequency)

      case (f, t) =>
        val currencyConversion = s"${f.name}-${t.name}"
        val currencyConversionSymOpt = cbrCurrencyRatesSource.getFinancialSymbol(currencyConversion)
        val conversionSeries = currencyConversionSymOpt match {
          case Some(ccs) => 
            ccs.closeValues

          case None =>
            val currencyConversion = s"${t.name}-${f.name}"
            val currencyConversionSymOpt = cbrCurrencyRatesSource.getFinancialSymbol(currencyConversion)
            currencyConversionSymOpt match {
              case Some(ccs) =>
                1.0 / ccs.closeValues

              case None =>
                ???
            }
        }

        series.as(periodFrequency)
              .leftJoin(conversionSeries.as(periodFrequency))
              .map(_ * _)
    }
  }

}
