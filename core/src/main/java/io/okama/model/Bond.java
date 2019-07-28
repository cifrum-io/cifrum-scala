package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Bond {
  public static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC();

  final boolean found;
  final BondInfo info;
  final BondActivities activities;

  @JsonCreator
  public Bond(@JsonProperty() boolean found,
              @JsonProperty() BondInfo info,
              @JsonProperty() BondActivities activities) {
    this.found = found;
    this.info = info;
    this.activities = activities;
  }

  public boolean isFound() {
    return found;
  }

  public BondInfo getInfo() {
    return info;
  }

  public BondActivities getActivities() {
    return activities;
  }

}
