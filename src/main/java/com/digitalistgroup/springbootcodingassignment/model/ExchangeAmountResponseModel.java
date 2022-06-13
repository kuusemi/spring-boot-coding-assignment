package com.digitalistgroup.springbootcodingassignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeAmountResponseModel {
    private String from;
    private String to;
    @JsonProperty("to_amount")
    private BigDecimal toAmount;
    @JsonProperty("exchange_rate")
    private BigDecimal exchangeRate;
}
