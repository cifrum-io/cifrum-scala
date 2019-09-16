package io.okama
package unit

import timeseries._

import org.joda.time.{convert => _, _}

trait Slice[T <: PeriodFrequency] {
  type PeriodType = T match {
    case PeriodFrequency.Day.type   => LocalDate
    case PeriodFrequency.Month.type => YearMonth
  }

  type ResultType = TimeSeries

  val frequency: T
  val startDate: PeriodType
  val endDate: PeriodType
}

object Slice {
  def daily(startDate: LocalDate, endDate: LocalDate): Slice[PeriodFrequency.Day.type] = {
    new Slice[PeriodFrequency.Day.type] {
      val frequency = PeriodFrequency.Day
      val startDate = startDate
      val endDate = endDate
    }
  }

  def monthly(startDate: YearMonth, endDate: YearMonth): Slice[PeriodFrequency.Month.type] = {
    new Slice[PeriodFrequency.Month.type] {
      val frequency = PeriodFrequency.Month
      val startDate = startDate
      val endDate = endDate
    }
  }
}
