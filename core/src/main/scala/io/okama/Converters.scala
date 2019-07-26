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

  def convert(bondOption: Option[Bond]): m.Bond = {
    bondOption match {
      case None =>
        m.Bond(false, null, null)
      case Some(b) =>
        val activities = m.BondActivities(b.yieldToMaturity)
        m.Bond(true, convert(b.info), activities)
    }
  }
}
