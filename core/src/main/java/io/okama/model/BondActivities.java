package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BondActivities {

    final double yieldToMaturity;

    @JsonCreator
    public BondActivities(@JsonProperty() double yieldToMaturity) {
        this.yieldToMaturity = yieldToMaturity;
    }

    public double getYieldToMaturity() {
        return yieldToMaturity;
    }
}
