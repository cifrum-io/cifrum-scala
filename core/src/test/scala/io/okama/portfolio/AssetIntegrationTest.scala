package io.okama
package portfolio

import portfolio.{AssetNamespace => ANS}
import timeseries._
import unit.{periodFrequencyDay, periodFrequencyMonth, _}

import com.google.inject.{Guice, AbstractModule}
import org.joda.time._
import verify._

object AssetIntegrationTest extends TestSuite[Common] {
  def setup(): Common = {
    val common = Common()
    common
  }

  def tearDown(env: Common): Unit = {
  }

  test("closeValues: just slice, no currency and period conversion") { c =>
    val asset: AssetMICEX = c.registry.get(AssetNamespace.micex, "SBER").get
    assert(asset.symbol.currency == Currency.rub)
    val startDate = LocalDate.parse("2012-4-1")
    val endDate   = LocalDate.parse("2014-1-1")
    val cv: TimeSeriesDay[Double] = asset.closeValues(
      Slice.daily(startDate=startDate, endDate=endDate),
      currency=Currency.rub,
    )
    assert(cv.index.values.head.equals(startDate))
    assert(cv.index.values.last.equals(endDate))
  }
}
