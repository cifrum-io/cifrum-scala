package io.okama
package portfolio

import unit._
import source._
import timeseries._

import javax.inject._

class Asset[T <: PeriodFrequency, TS <: TimeSeries[T, Double], FT <: FinancialSymbol[T, TS]](
  symbol: FT, currencyRegistry: CurrencyRegistry
) {

  def closeValues[V <: PeriodFrequency](
    slice: Slice[V], currency: Currency
  ) (given periodFrequency: V): TimeSeriesResult[V] = {
    val cvToCurrency = currencyRegistry.convert(series=symbol.closeValues, from=symbol.currency, to=currency)
    cvToCurrency.as(periodFrequency)
  }

  override def toString(): String = {
    s"Asset(symobl=$symbol)"
  }

}

sealed trait AssetNamespace

object AssetNamespace {
  case class US()    extends AssetNamespace
  case class MICEX() extends AssetNamespace

  val us    = US()
  val micex = MICEX()
}

type AssetUS    = Asset[PeriodFrequency.Month, VectorEomSeries[Double], UsFinancialSymbol]
type AssetMICEX = Asset[PeriodFrequency.Day, VectorEodSeries[Double], MicexStockFinancialSymbol]

type AssetType[T <: AssetNamespace] = T match {
  case AssetNamespace.US    => AssetUS
  case AssetNamespace.MICEX => AssetMICEX
}

class Registry @Inject(
  usDataSource: UsDataSource,
  micexStockDataSource: MicexStockDataSource,
  currencyRegistry: CurrencyRegistry,
) {
  def get[T <: AssetNamespace](namespace: T, code: String): Option[AssetType[T]] = {
    namespace match {
      case AssetNamespace.us =>
        val symOpt = usDataSource.getFinancialSymbol(code=code)
        symOpt.map { sym => 
          val res = AssetUS(symbol=sym, currencyRegistry=currencyRegistry) 
          res.asInstanceOf[AssetType[T]]
        }
      case AssetNamespace.micex =>
        val symOpt = micexStockDataSource.getFinancialSymbol(code=code)
        symOpt.map { sym => 
          val res = AssetMICEX(symbol=sym, currencyRegistry=currencyRegistry) 
          res.asInstanceOf[AssetType[T]]
        }
    }
  }
}
