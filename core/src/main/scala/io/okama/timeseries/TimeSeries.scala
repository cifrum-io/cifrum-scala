package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

/**
 * K - index type
 * T - values type
 */
trait TimeSeries[K <: PeriodFrequency, T] {
  val frequency: PeriodFrequency
  type IndexType = K match {
    case PeriodFrequency.Day   => LocalDate
    case PeriodFrequency.Month => YearMonth
  }

  case class TimeSeriesIndex(frequency: PeriodFrequency, values: Vector[IndexType])

  def at(k: IndexType): Option[T]
  def as[T <: PeriodFrequency](frequency: T): TimeSeriesResult[T]
  def index: TimeSeriesIndex
  def values: Vector[T]

  def map[V](f: T => V): TimeSeries[K, V]
  def zip[V](ts: TimeSeries[K, V]): TimeSeries[K, (T, V)]
}

type TimeSeriesDay[T]   = TimeSeries[PeriodFrequency.Day, T]
type TimeSeriesMonth[T] = TimeSeries[PeriodFrequency.Month, T]
