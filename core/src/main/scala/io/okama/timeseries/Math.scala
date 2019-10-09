package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

object Math {
  def mul[T <: PeriodFrequency](lhs: TimeSeries[T, Double], rhs: TimeSeries[T, Double]): TimeSeries[T, Double] = {
    assert(lhs.index.eql(rhs.index))
    lhs.zip(rhs).map(_ * _)
  }

  def div[T <: PeriodFrequency](v: Double, rhs: TimeSeries[T, Double]): TimeSeries[T, Double] = {
    rhs.map(v / _)
  }

  def div[T <: PeriodFrequency](rhs: TimeSeries[T, Double], v: Double): TimeSeries[T, Double] = {
    rhs.map(_ / v)
  }
}
