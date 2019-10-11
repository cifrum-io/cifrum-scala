package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

import unit.{periodFrequencyDay, periodFrequencyMonth}

type IndexTypeF[K <: PeriodFrequency] <: org.joda.time.base.AbstractPartial = K match {
  case PeriodFrequency.Day   => LocalDate
  case PeriodFrequency.Month => YearMonth
}

case class TimeSeriesIndex[T <: PeriodFrequency](values: Vector[IndexTypeF[T]])(given freq: T) {
  val frequency: T = freq

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

  val frequency: K

  def at(t: IndexTypeF[K]): Option[T]
  def as[V <: PeriodFrequency](frequency: V): TimeSeries[V, T]
  def index: TimeSeriesIndex[K]
  def values: Vector[T]

  def head: (I, T) = (index.values.head, values.head)
  def map[V](f: T => V): TimeSeries[K, V]
  def zip[V](ts: TimeSeries[K, V]): TimeSeries[K, (T, V)]
  def filterIndex(f: IndexTypeF[K] => Boolean): TimeSeries[K, T]
}

type TimeSeriesDay[T]   = TimeSeries[PeriodFrequency.Day, T]
type TimeSeriesMonth[T] = TimeSeries[PeriodFrequency.Month, T]

object TimeSeries {

  def apply[K <: PeriodFrequency, T](data: Vector[(IndexTypeF[K], T)])(given freq: K): TimeSeries[K, T] = {
    freq match {
      case PeriodFrequency.day =>
        val data1 = data.asInstanceOf[Vector[(LocalDate, T)]]
        VectorEodSeries[T](data1).asInstanceOf[TimeSeries[K, T]]

      case PeriodFrequency.month =>
        val data1 = data.asInstanceOf[Vector[(YearMonth, T)]]
        VectorEomSeries[T](data1).asInstanceOf[TimeSeries[K, T]]
    }
  }

}