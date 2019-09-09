package io.okama
package source

import unit._

import scala.io.Source
import com.github.tototoshi.csv._
import com.typesafe.config.ConfigFactory

abstract class FinancialSymbolsSource(val namespace: String) {
  def getFinancialSymbol(name: String): Option[FinancialSymbol]
}

case class UsFinancialSymbol(
  identifier: FinancialSymbolId,
  name: String,
  currency: Currency,
  periodFrequency: PeriodFrequency,
  country: String,
  exchange: String,
  kind: String,
) extends FinancialSymbol

class UsDataSource() extends FinancialSymbolsSource(namespace="us") {
  private val conf = ConfigFactory.load()

  private val symbolCodeToSymbolMap: Map[String, UsFinancialSymbol] = {
    val metaInfoStream = {
      val dataUrl = conf.getString("okama.data-url")
      val path = conf.getString("okama.us-data-path")
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
          name=name,
          currency=Currency.valueOf(currency),
          periodFrequency=PeriodFrequency.Day,
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

  def getFinancialSymbol(name: String): Option[UsFinancialSymbol] = {
    symbolCodeToSymbolMap.get(name)
  }

}
