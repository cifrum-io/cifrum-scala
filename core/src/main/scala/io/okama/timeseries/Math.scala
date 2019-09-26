package io.okama
package timeseries

import unit._

import org.joda.time.{convert => _, _}
import com.github.tototoshi.csv._
import scala.io.Source

object Math {
  def mul[T <: PeriodFrequency](lhs: TimeSeries[T, Double], rhs: TimeSeries[T, Double]): TimeSeries[T, Double] = {
    lhs.zip(rhs).map(_ * _)
  }
}
