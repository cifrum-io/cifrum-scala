package io
package okama

import bonds._
import okama.{model => m}

import collection.JavaConverters._

object Converters {
  def convert(bondsMeta: BondsMeta): m.BondsMeta = {
    val bis = bondsMeta.infos.map { bi => m.BondInfo(bi.isin, bi.name) }
    m.BondsMeta(bis.asJava)
  }

  def convert(bondInfo: BondInfo): m.BondInfo = {
    m.BondInfo(bondInfo.isin, bondInfo.name)
  }

  def convert(bondCoupon: BondCoupon): m.BondCoupon = {
    m.BondCoupon(bondCoupon.date, bondCoupon.periodDays, bondCoupon.rate, bondCoupon.value)
  }

  def convert(yieldToMaturityOption: Option[BondYieldToMaturity]): m.BondYieldToMaturity = {
    yieldToMaturityOption match {
      case Some(yieldToMaturity) =>
        m.BondYieldToMaturity(
          yieldToMaturity.value,
          yieldToMaturity.faceValue,
          yieldToMaturity.price,
          yieldToMaturity.buyDate)
      case None => null
    }
  }

  def convert(bondOption: Option[Bond]): m.Bond = {
    bondOption match {
      case None =>
        m.Bond(false, null, null)
      case Some(b) =>
        val coupons = b.coupons.map { convert }.asJava
        val ytm = convert(b.yieldToMaturity)
        val activities = m.BondActivities(ytm, coupons)
        m.Bond(true, convert(b.info), activities)
    }
  }
}
