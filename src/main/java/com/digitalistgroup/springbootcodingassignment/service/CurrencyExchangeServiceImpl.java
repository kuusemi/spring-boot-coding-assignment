package com.digitalistgroup.springbootcodingassignment.service;

import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService{
    @Override
    public ExchangeAmountResponseModel exchangeCurrency(ExchangeAmountRequestModel exchangeAmountRequestModel) {
        return new ExchangeAmountResponseModel(
                "EUR", "SEK", BigDecimal.valueOf(143.3890d), BigDecimal.valueOf(1509.34d)
        );
    }
}
