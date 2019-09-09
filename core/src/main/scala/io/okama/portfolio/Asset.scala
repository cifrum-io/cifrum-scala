package io.okama
package portfolio

import unit._

class Asset(symbol: FinancialSymbol) {

  def closeValues[T <: PeriodFrequency](slice: Slice[T], currency: Currency): Slice[T]#ResultType = {
    Vector(1, 2, 3)
  }

}

object Asset {
  def get(name: String): Asset = Asset(null)
}
