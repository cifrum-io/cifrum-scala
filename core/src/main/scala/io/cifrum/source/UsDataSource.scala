package io.cifrum
package source

import unit._
import timeseries._

import scala.io.Source
import com.github.tototoshi.csv._
import com.typesafe.config.{Config, ConfigFactory}
import java.time.format._
import java.time.temporal._

case class UsFinancialSymbol(
  identifier: FinancialSymbolId,
  code: String,
  name: String,
  currency: Currency,
  country: String,
  exchange: String,
  kind: String,
) extends FinancialSymbol[PeriodFrequency.Month, TimeSeriesMonth[Double]](periodFrequency=PeriodFrequency.month) {

  def closeValues: VectorEomSeries[Double] = {
    VectorEomSeries.fromCsv(
      url=UsDataSource.closeValuesUrl(code), 
      dateColumn="period", 
      valueColumn="close",
    )
  }

}

class UsDataSource() extends FinancialSymbolsSource[PeriodFrequency.Month, TimeSeriesMonth[Double], UsFinancialSymbol](namespace="us") {
  private val symbolCodeToSymbolMap: Map[String, UsFinancialSymbol] = {
    val metaInfoStream = {
      val source = Source.fromURL(UsDataSource.metaInfoUrl)
      val csvReader = CSVReader.open(source)
      csvReader.toStream
    }

    val csvHeader = metaInfoStream.head
    assert(csvHeader.toSet == Set("Code", "Name", "Country", "Exchange", "Currency", "Type"))

    val csvContent = metaInfoStream.tail
    val symbolCodeToSymbolMap = csvContent.foldLeft(Map.empty[String, UsFinancialSymbol]) {
      case (m, List(code, name, country, exchange, currency, itemType)) => 
        val sym = UsFinancialSymbol(
          identifier=FinancialSymbolId(namespace=namespace, code=code),
          code=code,
          name=name,
          currency=Currency.valueOf(currency),
          country=country,
          exchange=exchange,
          kind=itemType,
        )
        m + (code -> sym)
      case (m, _) => 
        m
    }
    symbolCodeToSymbolMap
  }

  def getFinancialSymbol(code: String): Option[UsFinancialSymbol] = {
    symbolCodeToSymbolMap.get(code)
  }

}

object UsDataSource {
  private val conf: Config = ConfigFactory.load()
  private val dataUrl: String = conf.getString("cifrum.data-url")
  private val path: String = conf.getString("cifrum.us-data-path")

  private val metaInfoUrl: String = dataUrl + path
  private[source] def closeValuesUrl(code: String): String =
    dataUrl + path + "/" + code
}
