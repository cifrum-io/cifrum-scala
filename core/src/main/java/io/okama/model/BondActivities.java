package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BondActivities {

  final double yieldToMaturity;
  final List<BondCoupon> coupons;

  @JsonCreator
  public BondActivities(@JsonProperty() double yieldToMaturity,
                        @JsonProperty() List<BondCoupon> coupons) {
    this.yieldToMaturity = yieldToMaturity;
    this.coupons = coupons;
  }

  public double getYieldToMaturity() {
    return yieldToMaturity;
  }

  public List<BondCoupon> getCoupons() {
    return coupons;
  }
}
