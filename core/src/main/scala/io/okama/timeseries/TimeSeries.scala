package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

type TimeSeriesResult[T <: PeriodFrequency] = T match {
  case PeriodFrequency.Day   => VectorEodSeries
  case PeriodFrequency.Month => VectorEomSeries
}

trait TimeSeries {
  type IndexType
  type ValueType

  def at(t: IndexType): Option[ValueType]
  def as[T <: PeriodFrequency](frequency: T): TimeSeriesResult[T]
}
