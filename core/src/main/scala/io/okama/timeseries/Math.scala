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

}
