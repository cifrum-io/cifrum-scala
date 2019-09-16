package io.okama
package unit

import timeseries._

case class FinancialSymbolId(namespace: String, code: String)

trait FinancialSymbol {
  val identifier: FinancialSymbolId
  val currency: Currency
  val periodFrequency: PeriodFrequency
  def closeValues: TimeSeries
}
