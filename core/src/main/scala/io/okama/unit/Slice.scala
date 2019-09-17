package io.okama
package unit

import timeseries._

import org.joda.time.{convert => _, _}

trait Slice[T <: PeriodFrequency] {
  type PeriodType = T match {
    case PeriodFrequency.Day   => LocalDate
    case PeriodFrequency.Month => YearMonth
  }

  val startDate: PeriodType
  val endDate: PeriodType
}

object Slice {
  def daily(startDate: LocalDate, endDate: LocalDate): Slice[PeriodFrequency.Day] = {
    new Slice[PeriodFrequency.Day] {
      val startDate = startDate
      val endDate = endDate
    }
  }

  def monthly(startDate: YearMonth, endDate: YearMonth): Slice[PeriodFrequency.Month] = {
    new Slice[PeriodFrequency.Month] {
      val startDate = startDate
      val endDate = endDate
    }
  }
}
