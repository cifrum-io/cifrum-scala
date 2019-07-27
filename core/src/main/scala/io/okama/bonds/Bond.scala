package io
package okama
package bonds

import scala.util.Properties
import java.nio.file._

import com.github.tototoshi.csv.{CSVFormat, CSVReader, TSVFormat}
import org.joda.{time => jt}

implicit val csvFormat: CSVFormat = new TSVFormat {}

enum MetasColumn(val name: String) {
  case Isin extends MetasColumn("isin")
  case Name extends MetasColumn("name")
}

enum BondColumn(val name: String) {
  case CouponDate       extends BondColumn("coupon_date")
  case CouponPeriodDays extends BondColumn("coupon_period_days")
  case CouponRate       extends BondColumn("coupon_rate")
  case CouponValue      extends BondColumn("coupon_value_USD")
  case Description      extends BondColumn("description")
}

case class BondInfo(isin: String, name: String)

class BondsMeta(val infos: Vector[BondInfo]) {
  def find(isin: String): Option[BondInfo] = {
    infos.find { _.isin == isin }
  }
}

case class BondCoupon(date: jt.DateTime, periodDays: Int, rate: Double, value: Double)

class Bond(val info: BondInfo, dataDir: String) {
  private val fmt = jt.format.DateTimeFormat.forPattern("dd.MM.yyyy")

  val coupons: Vector[BondCoupon] = {
    val path = Paths.get(dataDir, "bonds", s"${info.isin}.tsv")
    val couponsCSV = CSVReader.open(path.normalize.toString).allWithHeaders()
    val coupons = couponsCSV.map { item =>
      val date = fmt.parseDateTime(item(BondColumn.CouponDate.name))
      val periodDays = item(BondColumn.CouponPeriodDays.name).toInt
      val rate = item(BondColumn.CouponRate.name).replace(',', '.').toDouble
      val value = item(BondColumn.CouponValue.name).toDouble
      BondCoupon(date = date, periodDays = periodDays, rate = rate, value = value)
    }
    coupons.toVector
  }

  def yieldToMaturity: Double = 0.42
}

object Bond {
  val dataDir = {
    val dd = Properties.envOrNone("DATA_DIR")
    assert(dd.isDefined, "$DATA_DIR must be defined")
    dd.get
  }

  val meta: BondsMeta = {
    val metaPath = Paths.get(dataDir, "bonds", "__meta.tsv")
    assert(Files.exists(metaPath), "Bonds meta must exist")

    val metaCSV = CSVReader.open(metaPath.normalize.toString).allWithHeaders()
    val bondInfos = metaCSV.map { item =>
      val isin = item(MetasColumn.Isin.name)
      val name = item(MetasColumn.Name.name)
      BondInfo(isin = isin, name = name)
    }

    BondsMeta(infos = bondInfos.toVector)
  }

  def find(isin: String): Option[Bond] = {
    meta.find(isin).map { b => Bond(b, dataDir) }
  }

}
