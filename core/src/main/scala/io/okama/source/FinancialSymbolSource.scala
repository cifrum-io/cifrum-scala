package io.okama
package source

import unit._
import timeseries._

abstract class FinancialSymbolsSource(val namespace: String) {
  def getFinancialSymbol(name: String): Option[FinancialSymbol]
}
