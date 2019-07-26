package io
package okama
package bonds

import scala.util.Properties
import java.nio.file._

import com.github.tototoshi.csv.{CSVFormat, CSVReader, TSVFormat}

enum MetasColumn(val name: String) {
  case Isin extends MetasColumn("isin")
  case Name extends MetasColumn("name")
}

case class BondInfo(isin: String, name: String)

class BondsMeta(val infos: Vector[BondInfo]) {
  def find(isin: String): Option[BondInfo] = {
    infos.find { _.isin == isin }
  }
}

class Bond(val info: BondInfo) {
  def yieldToMaturity: Double = 0.42
}

object Bond {

  val meta: BondsMeta = {
    val dataDir = Properties.envOrNone("DATA_DIR")
    assert(dataDir.isDefined, "$DATA_DIR must be defined")

    val metaPath = Paths.get(dataDir.get, "bonds", "__meta.tsv")
    assert(Files.exists(metaPath), "Bonds meta must exist")

    implicit val csvFormat: CSVFormat = new TSVFormat {}
    val metaCSV = CSVReader.open(metaPath.normalize.toString).allWithHeaders()
    val bondInfos = metaCSV.map { item =>
      val isin = item(MetasColumn.Isin.name)
      val name = item(MetasColumn.Name.name)
      BondInfo(isin = isin, name = name)
    }

    BondsMeta(infos = bondInfos.toVector)
  }

  def find(isin: String): Option[Bond] = {
    meta.find(isin).map { b => Bond(b) }
  }

}
