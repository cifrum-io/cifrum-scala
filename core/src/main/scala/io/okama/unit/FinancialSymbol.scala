package io.okama
package unit

import timeseries._

case class FinancialSymbolId(namespace: String, code: String)

trait FinancialSymbol[T <: PeriodFrequency, TS <: TimeSeries[T, Double]](val periodFrequency: T) {
  val identifier: FinancialSymbolId
  val currency: Currency
  def closeValues: TS
}
