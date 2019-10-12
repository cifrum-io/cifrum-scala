package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

given timeSeriesOps: {

  def (lhs: TimeSeries[T, Double]) *[T <: PeriodFrequency] (rhs: TimeSeries[T, Double]): TimeSeries[T, Double] = {
    assert(lhs.index.eql(rhs.index))
    lhs.zip(rhs).map(_ * _) 
  }

  def (v: Double) /[T <: PeriodFrequency] (ts: TimeSeries[T, Double]): TimeSeries[T, Double] = {
    ts.map(v / _)
  }

  def (ts: TimeSeries[T, Double]) /[T <: PeriodFrequency] (v: Double): TimeSeries[T, Double] = {
    ts.map(_ / v)
  }

  def (ts: TimeSeries[T, V]) alignToIndex[T <: PeriodFrequency, V] (index: TimeSeriesIndex[T])(given T) = {
    var prev = ts.head._2
    val data = index.values.map { i =>
      val v = ts.at(i)
      if (v.isDefined) {
        prev = v.get
      }
      (i, prev)
    }
    TimeSeries(data)
  }

  def (lhs: TimeSeries[T, V1]) leftJoin[T <: PeriodFrequency, V1, V2] (rhs: TimeSeries[T, V2]) (given T): TimeSeries[T, (V1, V2)] = {
    val rhs1 = rhs.alignToIndex(lhs.index)
    lhs.zip(rhs1)
  }

}
