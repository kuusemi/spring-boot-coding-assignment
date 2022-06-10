package com.digitalistgroup.springbootcodingassignment.service;

import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final Environment environment;

    private final RestTemplate restTemplate;

    @Override
    public ExchangeAmountResponseModel exchangeCurrency(ExchangeAmountRequestModel exchangeAmountRequestModel) {
        return new ExchangeAmountResponseModel(
                "EUR", "SEK", BigDecimal.valueOf(143.3890d), BigDecimal.valueOf(1509.34d)
        );
    }

    @Override
    public void storeExchangeRates() {
        String exchangeRatesServiceUrl = environment.getProperty("exchange.rates.url");
        String exchangeRatesServiceApiKey = environment.getProperty("exchange.rates.apiKey");
    }
}
