package com.digitalistgroup.springbootcodingassignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseCurrency {
    @JsonProperty(value = "base")
    private String baseCurrency;
    @JsonProperty(value = "rates")
    private Map<String, BigDecimal> exchangeRates;
}
