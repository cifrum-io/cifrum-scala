package io.cifrum
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

trait TimeSeriesOps {

  def[T <: PeriodFrequency] (lhs: TimeSeries[T, Double]) * (rhs: TimeSeries[T, Double]): TimeSeries[T, Double] = {
    assert(lhs.index.eql(rhs.index))
    lhs.zip(rhs).map(_ * _) 
  }

  def[T <: PeriodFrequency] (v: Double) / (ts: TimeSeries[T, Double]): TimeSeries[T, Double] = {
    ts.map(v / _)
  }

  def[T <: PeriodFrequency] (ts: TimeSeries[T, Double]) / (v: Double): TimeSeries[T, Double] = {
    ts.map(_ / v)
  }

  def[T <: PeriodFrequency, V] (ts: TimeSeries[T, V]) alignToIndex (index: TimeSeriesIndex[T])(given T) = {
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

  def[T <: PeriodFrequency, V1, V2] (lhs: TimeSeries[T, V1]) leftJoin (rhs: TimeSeries[T, V2]) (given T): TimeSeries[T, (V1, V2)] = {
    val rhs1 = rhs.alignToIndex(lhs.index)
    lhs.zip(rhs1)
  }

}

given timeSeriesOps: TimeSeriesOps
