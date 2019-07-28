package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BondCoupon {
  final DateTime date;
  final int periodDays;
  final double rate;
  final double value;
  DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

  @JsonCreator
  public BondCoupon(@JsonProperty() DateTime date,
                    @JsonProperty() int periodDays,
                    @JsonProperty() double rate,
                    @JsonProperty() double value) {
    this.date = date;
    this.periodDays = periodDays;
    this.rate = rate;
    this.value = value;
  }

  public String getDate() {
    return fmt.print(date);
  }

  public int getPeriodDays() {
    return periodDays;
  }

  public double getRate() {
    return rate;
  }

  public double getValue() {
    return value;
  }
}
