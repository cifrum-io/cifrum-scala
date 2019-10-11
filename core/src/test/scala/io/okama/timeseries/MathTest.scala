package io.okama
package timeseries

import portfolio.{AssetNamespace => ANS}
import timeseries._
import unit.{periodFrequencyDay, periodFrequencyMonth, PeriodFrequency => PF, _}

import com.google.inject.{Guice, AbstractModule}
import org.joda.time.{convert => _, _}
import verify._
import timeseries.timeSeriesOps

object AssetIntegrationTest extends BasicTestSuite {
  def ld(s: String): LocalDate = {
    LocalDate.parse(s)
  }

  test("alignToIndex") {
    val ts = VectorEodSeries(data=Vector((ld("2000-1-1"), 1), (ld("2000-1-3"), 2)))
    val dates = Vector(ld("2000-1-1"), ld("2000-1-2"), ld("2000-1-3"))
    val index = TimeSeriesIndex[PF.Day](values=dates)

    val v1 = ts.at(ld("2000-1-2")).isEmpty
    assert(v1)

    val v2 = ts.alignToIndex(index=index).at(ld("2000-1-2")).equals(Option(1))
    assert(v2)
  }

}
