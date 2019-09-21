package io.okama
package source

import unit._
import timeseries._

import scala.io.Source
import com.github.tototoshi.csv._
import com.typesafe.config.ConfigFactory
import java.time.format._
import java.time.temporal._

case class MicexStockFinancialSymbol(
  identifier: FinancialSymbolId,
  code: String,
  name: String,
  currency: Currency,
  periodFrequency: PeriodFrequency,
) extends FinancialSymbol {

  def closeValues: VectorEodSeries = {
    VectorEodSeries.fromCsv(
      url=MicexStockDataSource.closeValuesUrl(code), 
      dateColumn="date", 
      valueColumn="adjusted_close",
    )
  }

}

class MicexStockDataSource() extends FinancialSymbolsSource(namespace="micex") {
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
          currency=Currency.RUB,
          periodFrequency=PeriodFrequency.day,
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
  private val conf: com.typesafe.config.Config = ConfigFactory.load()
  private val dataUrl: String = conf.getString("okama.data-url")
  private val path: String = conf.getString("okama.micex_stocks-data-path")

  private val metaInfoUrl: String = dataUrl + path + "/__index.csv"
  private[source] def closeValuesUrl(code: String): String =
    dataUrl + path + "/" + code + ".csv"
}
