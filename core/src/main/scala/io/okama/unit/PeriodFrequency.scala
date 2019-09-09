package io.okama
package unit

sealed trait PeriodFrequency 

object PeriodFrequency {
  case object Day     extends PeriodFrequency
  case object Decade  extends PeriodFrequency
  case object Month   extends PeriodFrequency
}
