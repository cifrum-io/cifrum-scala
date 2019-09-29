package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

class VectorEodSeries[T](data: Vector[(LocalDate, T)]) extends TimeSeriesDay[T] {
  type IndexType = LocalDate

  val frequency: PeriodFrequency = PeriodFrequency.day

  def values: Vector[T] = {
    data.map(_._2)
  }

  def at(t: LocalDate): Option[T] = {
    data.find((d, _) => d.equals(t)).map((_, v) => v)
  }

  def as[V <: PeriodFrequency](frequency: V): TimeSeries[V, T] = {
    val result = frequency match {
      case PeriodFrequency.day =>
        this
      case PeriodFrequency.month =>
        ???
      case PeriodFrequency.decade =>
        ???
    }
    result.asInstanceOf[TimeSeries[V, T]]
  }

  def index: TimeSeriesIndex = 
    TimeSeriesIndex(frequency=PeriodFrequency.day, values=data.map(_._1))

  def map[V](f: T => V): TimeSeries[PeriodFrequency.Day, V] = { 
    val values1 = values.map(f)
    val data1 = index.values.zip(values1)
    VectorEodSeries[V](data1)
  }

  def zip[V](ts: TimeSeries[PeriodFrequency.Day, V]): TimeSeries[PeriodFrequency.Day, (T, V)] = {
    val values1 = values.zip(ts.values)
    val data1 = index.values.zip(values1)
    VectorEodSeries(data1)
  }

  override def toString() = 
    data match {
      case Vector() => "VectorEodSeries()"
      case ds       => s"VectorEodSeries(${ds.head} .. ${ds.last})"
    }
}

object VectorEodSeries {
  def fromCsv(url: String, dateColumn: String, valueColumn: String): VectorEodSeries[Double] = {
    val items = CSVReader.open(Source.fromURL(url))(new TSVFormat{}).allWithHeaders
    val data =
      for (item <- items) yield {
        val d = LocalDate.parse(item(dateColumn))
        val v = item(valueColumn).toDouble
        (d, v)
      }
    val dataSorted = data.toVector.sortWith { case ((d1, _), (d2, _)) => d1.isBefore(d2) }
    VectorEodSeries[Double](dataSorted)
  }
}
