package io.okama
package unit

import timeseries._

import org.joda.time.{convert => _, _}

trait Slice[T <: PeriodFrequency] {
  type PeriodType <: org.joda.time.base.AbstractPartial = T match {
    case PeriodFrequency.Day   => LocalDate
    case PeriodFrequency.Month => YearMonth
  }

  val startDate: PeriodType
  val endDate: PeriodType
}

object Slice {
  def daily(startDate: LocalDate, endDate: LocalDate): Slice[PeriodFrequency.Day] = {
    val (sd, ed) = (startDate, endDate)
    new Slice[PeriodFrequency.Day] {
      val startDate = sd
      val endDate = ed
    }
  }

  def monthly(startDate: YearMonth, endDate: YearMonth): Slice[PeriodFrequency.Month] = {
    val (sd, ed) = (startDate, endDate)
    new Slice[PeriodFrequency.Month] {
      val startDate = sd
      val endDate = ed
    }
  }
}
