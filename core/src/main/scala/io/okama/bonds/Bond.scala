package io.okama
package bonds

object Bond {

  def compute(isin: String): model.Bond = {
    new model.Bond(isin, 0.42)
  }

}
