package io.cifrum
package source

import unit._
import timeseries._

import scala.io.Source
import com.github.tototoshi.csv._
import com.typesafe.config.{Config, ConfigFactory}
import java.time.format._
import java.time.temporal._

case class CbrCurrencyRatesFinancialSymbol(
  identifier: FinancialSymbolId,
  targetCurrency: Currency,
  currency: Currency,
) extends FinancialSymbol[PeriodFrequency.Day, TimeSeriesDay[Double]](periodFrequency=PeriodFrequency.day) {

  def closeValues: TimeSeriesDay[Double] = {
    val url = CbrCurrencyRatesSource.closeValuesUrl(s"${targetCurrency.name}-${currency.name}")
    VectorEodSeries.fromCsv(
      url=url, 
      dateColumn="date", 
      valueColumn="close",
    )
  }

}

class CbrCurrencyRatesSource() extends FinancialSymbolsSource[PeriodFrequency.Day, TimeSeriesDay[Double], CbrCurrencyRatesFinancialSymbol](namespace="cbr") {
  private val symbolCodeToSymbolMap: Map[String, CbrCurrencyRatesFinancialSymbol] = {
    val metaInfoStream = {
      val source = Source.fromURL(CbrCurrencyRatesSource.metaInfoUrl)
      val csvReader = CSVReader.open(source)(new TSVFormat {})
      csvReader.toStream
    }

    val csvHeader = metaInfoStream.head
    assert(csvHeader.toSet == Set("name", "date_start", "date_end"))

    val csvContent = metaInfoStream.tail
    val symbolCodeToSymbolMap = csvContent.foldLeft(Map.empty[String, CbrCurrencyRatesFinancialSymbol]) {
      case (m, List(code, dateStart, dateEnd)) => 
        val Array(from, to) = code.split('-')
        assert(to == "RUB")
        val sym = CbrCurrencyRatesFinancialSymbol(
          identifier=FinancialSymbolId(namespace=namespace, code=code),
          targetCurrency=Currency.valueOf(from),
          currency=Currency.valueOf(to),
        )
        m + (code -> sym)
      case (m, _) => 
        m
    }
    symbolCodeToSymbolMap
  }

  def getFinancialSymbol(code: String): Option[CbrCurrencyRatesFinancialSymbol] = {
    symbolCodeToSymbolMap.get(code)
  }

}

object CbrCurrencyRatesSource {
  private val conf: Config = ConfigFactory.load()
  private val dataUrl: String = conf.getString("cifrum.data-url")
  private val path: String = conf.getString("cifrum.cbr_currency-data-path")

  private val metaInfoUrl: String = dataUrl + path + "/__index.csv"
  private[source] def closeValuesUrl(code: String): String =
    dataUrl + path + "/" + code + ".csv"
}
