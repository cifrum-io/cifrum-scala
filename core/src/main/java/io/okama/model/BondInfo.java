package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BondInfo {

  final String isin;
  final String name;

  @JsonCreator
  public BondInfo(@JsonProperty() String isin,
                  @JsonProperty() String name) {
    this.isin = isin;
    this.name = name;
  }

  public String getIsin() {
    return isin;
  }

  public String getName() {
    return name;
  }
}
