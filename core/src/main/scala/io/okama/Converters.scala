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
}
