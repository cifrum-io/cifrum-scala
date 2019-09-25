package io.okama
package source

import unit._
import timeseries._

abstract class FinancialSymbolsSource[T <: PeriodFrequency, TS <: TimeSeries[T, Double], FT <: FinancialSymbol[T, TS]](val namespace: String) {
  def getFinancialSymbol(code: String): Option[FT]
}
