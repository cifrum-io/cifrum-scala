package io.okama
package unit

case class FinancialSymbolId(namespace: String, code: String)

trait FinancialSymbol {
  val identifier: FinancialSymbolId
  val currency: Currency
  val periodFrequency: PeriodFrequency
}
