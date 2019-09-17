package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

type VectorEomDataType = Vector[(YearMonth, Double)]

class VectorEomSeries(data: VectorEomDataType) extends TimeSeries {
  type IndexType = YearMonth
  type ValueType = Double

  val values: VectorEomDataType = 
    data.toVector.sortWith { case ((d1, _), (d2, _)) => d1.isBefore(d2) }

  def at(t: YearMonth): Option[Double] = {
    values.find((d, _) => d.equals(t)).map((_, v) => v)
  }

  def as[T <: PeriodFrequency](frequency: T): TimeSeriesResult[T] = {
    val result = frequency match {
      case PeriodFrequency.day => 
        val data = Vector((LocalDate.parse("2019-1-1"), 10.1))
        VectorEodSeries(data)
      case PeriodFrequency.month => 
        this
      case PeriodFrequency.decade =>
        ???
    }
    result.asInstanceOf[TimeSeriesResult[T]]
  }

  override def toString(): String = 
    values match {
      case Vector() => "VectorEomSeries()"
      case v        => s"VectorEomSeries(${v.head} .. ${v.last})"
    }
 
}

object VectorEomSeries {
  def fromCsv(url: String, dateColumn: String, valueColumn: String): VectorEomSeries = {
    val items = CSVReader.open(Source.fromURL(url))(new TSVFormat{}).allWithHeaders
    val data = 
      for (item <- items) yield {
        val d = YearMonth.parse(item(dateColumn))
        val v = item(valueColumn).toDouble
        (d, v)
      }
    VectorEomSeries(data.toVector)
  }
}
