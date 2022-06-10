package com.digitalistgroup.springbootcodingassignment.model;

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
    private BigDecimal toAmount;
    private BigDecimal exchangeRate;
}
