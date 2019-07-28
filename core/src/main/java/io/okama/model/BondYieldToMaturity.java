package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class BondYieldToMaturity {

  final double value;
  final double faceValue;
  final double price;
  final DateTime buyDate;

  @JsonCreator
  public BondYieldToMaturity(@JsonProperty() double value,
                             @JsonProperty() double faceValue,
                             @JsonProperty() double price,
                             @JsonProperty() DateTime buyDate) {
    this.value = value;
    this.faceValue = faceValue;
    this.price = price;
    this.buyDate = buyDate;
  }

  public double getValue() {
    return value;
  }

  public double getFaceValue() {
    return faceValue;
  }

  public double getPrice() {
    return price;
  }

  public String getBuyDate() {
    return Bond.formatter.print(buyDate);
  }
}
