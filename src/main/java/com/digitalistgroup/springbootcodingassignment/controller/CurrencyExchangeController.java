package com.digitalistgroup.springbootcodingassignment.controller;

import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;
import com.digitalistgroup.springbootcodingassignment.service.CurrencyExchangeService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/exchange_amount")
@Validated
@RequiredArgsConstructor
public class CurrencyExchangeController {

    private final CurrencyExchangeService exchangeService;

    @GetMapping(
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ExchangeAmountResponseModel> exchangeCurrency(
            @RequestParam("from") @Pattern(
                    regexp = "^EUR|SEK|USD$",
                    message = "Only following currency codes are supported: 'EUR', 'SEK' and 'USD'"
            ) String from,
            @RequestParam("to") @Pattern(
                    regexp = "^EUR|SEK|USD$",
                    message = "Only following currency codes are supported: 'EUR', 'SEK' and 'USD'"
            ) String to,
            @RequestParam("from_amount") @NotNull(
                    message = "Exchanged amount can't be empty"
            ) BigDecimal fromAmount) {

        ExchangeAmountRequestModel exchangeAmountRequestModel = new ExchangeAmountRequestModel(from, to, fromAmount);

        ExchangeAmountResponseModel exchangeAmountResponseModel =
                exchangeService.exchangeCurrency(exchangeAmountRequestModel);

        return ResponseEntity.status(HttpStatus.OK).body(exchangeAmountResponseModel);
    }
}
