package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

class VectorEomSeries[T](data: Vector[(YearMonth, T)]) extends TimeSeriesMonth[T] {
  type IndexType = YearMonth

  val frequency: PeriodFrequency = PeriodFrequency.month

  def values: Vector[T] = {
    data.map(_._2)
  }

  def at(t: YearMonth): Option[T] = {
    data.find((d, _) => d.equals(t)).map((_, v) => v)
  }

  def as[V <: PeriodFrequency](frequency: V): TimeSeries[V, T] = {
    val result = frequency match {
      case PeriodFrequency.day => 
        val data = Vector((LocalDate.parse("2019-1-1"), 10.1))
        VectorEodSeries(data)
      case PeriodFrequency.month => 
        this
      case PeriodFrequency.decade =>
        ???
    }
    result.asInstanceOf[TimeSeries[V, T]]
  }

  def index: TimeSeriesIndex = 
    TimeSeriesIndex(frequency=PeriodFrequency.month, values=data.map(_._1))
  
  def map[V](f: T => V): TimeSeries[PeriodFrequency.Month, V] = {
    val values1 = values.map(f)
    val data1 = index.values.zip(values1)
    VectorEomSeries(data1)
  }

  def zip[V](ts: TimeSeries[PeriodFrequency.Month, V]): TimeSeries[PeriodFrequency.Month, (T, V)] = {
    val values1 = values.zip(ts.values)
    val data1 = index.values.zip(values1)
    VectorEomSeries(data1)
  }

  override def toString(): String = {
    data match {
      case Vector() => "VectorEomSeries()"
      case ds       => s"VectorEomSeries(${ds.head} .. ${ds.last})"
    }
  }
 
}

object VectorEomSeries {
  def fromCsv(url: String, dateColumn: String, valueColumn: String): VectorEomSeries[Double] = {
    val items = CSVReader.open(Source.fromURL(url))(new TSVFormat{}).allWithHeaders
    val data = 
      for (item <- items) yield {
        val d = YearMonth.parse(item(dateColumn))
        val v = item(valueColumn).toDouble
        (d, v)
      }
    val dataSorted = data.toVector.sortWith { case ((d1, _), (d2, _)) => d1.isBefore(d2) }
    VectorEomSeries[Double](dataSorted.toVector)
  }
}
