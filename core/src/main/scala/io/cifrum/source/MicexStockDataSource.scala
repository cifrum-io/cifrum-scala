package io.cifrum
package source

import unit._
import timeseries._

import scala.io.Source
import com.github.tototoshi.csv._
import com.typesafe.config.{Config, ConfigFactory}
import java.time.format._
import java.time.temporal._

case class MicexStockFinancialSymbol(
  identifier: FinancialSymbolId,
  code: String,
  name: String,
  currency: Currency,
) extends FinancialSymbol[PeriodFrequency.Day, TimeSeriesDay[Double]](periodFrequency=PeriodFrequency.day) {

  def closeValues: TimeSeriesDay[Double] = {
    VectorEodSeries.fromCsv(
      url=MicexStockDataSource.closeValuesUrl(code), 
      dateColumn="date", 
      valueColumn="adjusted_close",
    )
  }

}

class MicexStockDataSource() extends FinancialSymbolsSource[PeriodFrequency.Day, TimeSeriesDay[Double], MicexStockFinancialSymbol](namespace="micex") {
  private val symbolCodeToSymbolMap: Map[String, MicexStockFinancialSymbol] = {
    val metaInfoStream = {
      val source = Source.fromURL(MicexStockDataSource.metaInfoUrl)
      val csvReader = CSVReader.open(source)(new TSVFormat {})
      csvReader.toStream
    }

    val csvHeader = metaInfoStream.head
    assert(csvHeader.toSet == Set("name", "date_start", "date_end", "short_name", "long_name", "isin"))

    val csvContent = metaInfoStream.tail
    val symbolCodeToSymbolMap = csvContent.foldLeft(Map.empty[String, MicexStockFinancialSymbol]) {
      case (m, List(code, dateStart, dateEnd, shortName, longName, isin)) => 
        val sym = MicexStockFinancialSymbol(
          identifier=FinancialSymbolId(namespace=namespace, code=code),
          code=code,
          name=shortName,
          currency=Currency.rub,
        )
        m + (code -> sym)
      case (m, _) => 
        m
    }
    symbolCodeToSymbolMap
  }

  def getFinancialSymbol(code: String): Option[MicexStockFinancialSymbol] = {
    symbolCodeToSymbolMap.get(code)
  }

}

object MicexStockDataSource {
  private val conf: Config = ConfigFactory.load()
  private val dataUrl: String = conf.getString("cifrum.data-url")
  private val path: String = conf.getString("cifrum.micex_stocks-data-path")

  private val metaInfoUrl: String = dataUrl + path + "/__index.csv"
  private[source] def closeValuesUrl(code: String): String =
    dataUrl + path + "/" + code + ".csv"
}
