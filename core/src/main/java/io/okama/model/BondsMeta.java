package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BondsMeta {

  final List<BondInfo> infos;

  @JsonCreator
  public BondsMeta(@JsonProperty() List<BondInfo> infos) {
    this.infos = infos;
  }

  public List<BondInfo> getInfos() {
    return infos;
  }
}
