package io.okama
package unit

given periodFrequencyDay: PeriodFrequency.Day     = PeriodFrequency.day
given periodFrequencyMonth: PeriodFrequency.Month = PeriodFrequency.month

sealed trait PeriodFrequency 

object PeriodFrequency {
  case class Day()     extends PeriodFrequency
  case class Decade()  extends PeriodFrequency
  case class Month()   extends PeriodFrequency

  val day    = Day()
  val decade = Decade()
  val month  = Month()
}
