package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BondActivities {

  final BondYieldToMaturity yieldToMaturity;
  final List<BondCoupon> coupons;

  @JsonCreator
  public BondActivities(@JsonProperty() BondYieldToMaturity yieldToMaturity,
                        @JsonProperty() List<BondCoupon> coupons) {
    this.yieldToMaturity = yieldToMaturity;
    this.coupons = coupons;
  }

  public BondYieldToMaturity getYieldToMaturity() {
    return yieldToMaturity;
  }

  public List<BondCoupon> getCoupons() {
    return coupons;
  }
}
