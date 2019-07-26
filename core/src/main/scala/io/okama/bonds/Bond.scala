package io.okama
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
  def exists(isin: String): Boolean = {
    infos.exists { _.isin == isin }
  }
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

  def compute(isin: String): model.Bond = {
    new model.Bond(isin, 0.42)
  }

}
