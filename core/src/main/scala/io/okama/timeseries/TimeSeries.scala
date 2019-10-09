package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

type IndexTypeF[K <: PeriodFrequency] <: org.joda.time.base.AbstractPartial = K match {
  case PeriodFrequency.Day   => LocalDate
  case PeriodFrequency.Month => YearMonth
}

case class TimeSeriesIndex[T <: PeriodFrequency](frequency: PeriodFrequency, values: Vector[IndexTypeF[T]]) {
  def eql(idx: TimeSeriesIndex[T]): Boolean = {
    frequency == idx.frequency &&
      values.size == idx.values.size &&
      values.zip(idx.values).forall((l, r) => l.equals(r))
  }
}

/**
 * K - index type
 * T - values type
 */
trait TimeSeries[K <: PeriodFrequency, T] {
  type I = IndexTypeF[K]

  val frequency: PeriodFrequency

  def at(t: IndexTypeF[K]): Option[T]
  def as[V <: PeriodFrequency](frequency: V): TimeSeries[V, T]
  def index: TimeSeriesIndex[K]
  def values: Vector[T]

  def map[V](f: T => V): TimeSeries[K, V]
  def zip[V](ts: TimeSeries[K, V]): TimeSeries[K, (T, V)]
  def filterIndex(f: IndexTypeF[K] => Boolean): TimeSeries[K, T]
}

type TimeSeriesDay[T]   = TimeSeries[PeriodFrequency.Day, T]
type TimeSeriesMonth[T] = TimeSeries[PeriodFrequency.Month, T]
