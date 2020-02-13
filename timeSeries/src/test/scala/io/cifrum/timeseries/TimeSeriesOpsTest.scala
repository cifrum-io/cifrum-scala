package io.cifrum
package timeseries

import timeseries._
import unit.{periodFrequencyDay, periodFrequencyMonth, PeriodFrequency => PF, _}

import com.google.inject.{Guice, AbstractModule}
import org.joda.time.{convert => _, _}
import verify._
import timeseries.timeSeriesOps

object TimeSeriesOpsTest extends BasicTestSuite {
  given Eql[LocalDate, LocalDate] = Eql.derived
  given Eql[(Int, Int), (Int, Int)] = Eql.derived

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

  test("leftJoin") {
    val ts1 = VectorEodSeries(data=Vector((ld("2000-1-1"), 1), (ld("2000-1-3"), 3)))
    val ts2 = VectorEodSeries(data=Vector((ld("2000-1-1"), 10), (ld("2000-1-2"), 20), (ld("2000-1-3"), 30)))
    val ts = ts1.leftJoin(ts2)

    val v1 = ts.index.values == Vector(ld("2000-1-1"), ld("2000-1-3"))
    assert(v1)

    val v2 = ts.values == Vector((1,10), (3, 30))
    assert(v2)
  }
  
}
