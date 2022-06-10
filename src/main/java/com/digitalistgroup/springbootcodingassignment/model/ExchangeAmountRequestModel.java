package com.digitalistgroup.springbootcodingassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeAmountRequestModel {
    @NotNull(message = "From currency code can't be empty")
    @Size(min = 3, max = 3, message = "Currency code should be 3 characters long")
    @Pattern(regexp = "^EUR|SEK|USD$", message = "Only following currency codes are supported: 'EUR', 'SEK' and 'USD'")
    private String from;

    @NotNull(message = "To currency code can't be empty")
    @Size(min = 3, max = 3, message = "Currency code should be 3 characters long")
    @Pattern(regexp = "^EUR|SEK|USD$", message = "Only following currency codes are supported: 'EUR', 'SEK' and 'USD'")
    private String to;

    @NotNull(message = "Exchanged amount can't be empty")
    private BigDecimal fromAmount;
}
