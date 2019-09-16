package io.okama
package timeseries

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

trait TimeSeries {
  type IndexType
  type ValueType

  def at(t: IndexType): Option[ValueType]
}

class VectorMonthSeries private (data: Vector[(YearMonth, Double)]) extends TimeSeries {
  type IndexType = YearMonth
  type ValueType = Double

  val values = data.toVector.sortWith { case ((d1, _), (d2, _)) => d1.isBefore(d2) }

  def at(t: YearMonth): Option[Double] = {
    values.find((d, _) => d.equals(t)).map((_, v) => v)
  }

  override def toString(): String = {
    if (values.isEmpty) {
      "VectorMonthSeries()"
    } else {
      s"VectorMonthSeries(${values.head} .. ${values.last})"
    }
  }
}

class VectorDaySeries private (data: Vector[(LocalDate, Double)]) extends TimeSeries {
  type IndexType = LocalDate
  type ValueType = Double

  val values = data.toVector.sortWith { case ((d1, _), (d2, _)) => d1.isBefore(d2) }

  def at(t: LocalDate): Option[Double] = {
    values.find((d, _) => d.equals(t)).map((_, v) => v)
  }
}

object VectorMonthSeries {
  def fromCsv(url: String, dateColumn: String, valueColumn: String): VectorMonthSeries = {
    val items = CSVReader.open(Source.fromURL(url))(new TSVFormat{}).allWithHeaders
    val data = 
      for (item <- items) yield {
        val d = YearMonth.parse(item(dateColumn))
        val v = item(valueColumn).toDouble
        (d, v)
      }
    VectorMonthSeries(data.toVector)
  }
}
