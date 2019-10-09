package io.okama
package portfolio

import unit._
import source._
import timeseries._

import javax.inject._
import Math._

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
                div(1.0, ccs.closeValues)

              case None =>
                ???
            }
        }

        // TODO: fix the shortcut with Joins
        val series1 = series.as(periodFrequency)
        val conversionSeries1 = conversionSeries.as(periodFrequency)
        val series2 = series1.filterIndex(x => conversionSeries1.at(x).isDefined)
        val conversionSeries2 = conversionSeries1.filterIndex(x => series1.at(x).isDefined)

        mul(series2, conversionSeries2)
    }
  }

}
