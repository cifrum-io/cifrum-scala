package io.okama
package source

import unit._
import timeseries._

import scala.io.Source
import com.github.tototoshi.csv._
import com.typesafe.config.ConfigFactory
import java.time.format._
import java.time.temporal._

abstract class FinancialSymbolsSource(val namespace: String) {
  def getFinancialSymbol(name: String): Option[FinancialSymbol]
}

private val conf = ConfigFactory.load()
private val dataUrl = conf.getString("okama.data-url")
private val path = conf.getString("okama.us-data-path")

case class UsFinancialSymbol(
  identifier: FinancialSymbolId,
  code: String,
  name: String,
  currency: Currency,
  periodFrequency: PeriodFrequency,
  country: String,
  exchange: String,
  kind: String,
) extends FinancialSymbol {

  def closeValues: VectorEomSeries = {
    val url = dataUrl + path + "/" + code
    val ves = VectorEomSeries.fromCsv(url=url, dateColumn="period", valueColumn="close")
    ves
  }

}

class UsDataSource() extends FinancialSymbolsSource(namespace="us") {
  private val symbolCodeToSymbolMap: Map[String, UsFinancialSymbol] = {
    val metaInfoStream = {
      val url = dataUrl + path
      CSVReader.open(Source.fromURL(url)).toStream
    }

    val csvHeader = metaInfoStream.head
    assert(csvHeader == List("Code", "Name", "Country", "Exchange", "Currency", "Type"))

    val csvContent = metaInfoStream.tail
    val symbolCodeToSymbolMap = csvContent.foldLeft(Map.empty[String, UsFinancialSymbol]) {
      case (m, List(code, name, country, exchange, currency, itemType)) => 
        val sym = UsFinancialSymbol(
          identifier=FinancialSymbolId(namespace=namespace, code=code),
          code=code,
          name=name,
          currency=Currency.valueOf(currency),
          periodFrequency=PeriodFrequency.day,
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
