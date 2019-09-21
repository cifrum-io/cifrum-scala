package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}

type VectorEodDataType = Vector[(LocalDate, Double)]

class VectorEodSeries(data: VectorEodDataType) extends TimeSeries {
  type IndexType = LocalDate
  type ValueType = Double

  val values: VectorEodDataType = 
    data.toVector.sortWith { case ((d1, _), (d2, _)) => d1.isBefore(d2) }

  def at(t: LocalDate): Option[Double] = {
    values.find((d, _) => d.equals(t)).map((_, v) => v)
  }

  def as[T <: PeriodFrequency](frequency: T): TimeSeriesResult[T] = {
    val result = frequency match {
      case PeriodFrequency.day =>
        this
      case PeriodFrequency.month =>
        ???
      case PeriodFrequency.decade =>
        ???
    }
    result.asInstanceOf[TimeSeriesResult[T]]
  }

  override def toString() = 
    values match {
      case Vector() => "VectorEodSeries()"
      case v        => s"VectorEodSeries(${v.head} .. ${v.last})"
    }
}
