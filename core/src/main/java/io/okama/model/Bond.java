package io.okama.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bond {

    final String isin;
    final double yieldToMaturity;

    @JsonCreator
    public Bond(@JsonProperty() String isin,
                @JsonProperty() double yieldToMaturity) {
        this.isin = isin;
        this.yieldToMaturity = yieldToMaturity;
    }

    public String getIsin() {
        return isin;
    }

    public double getYieldToMaturity() {
        return yieldToMaturity;
    }

}
