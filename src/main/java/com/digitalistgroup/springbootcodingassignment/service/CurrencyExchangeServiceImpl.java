package com.digitalistgroup.springbootcodingassignment.service;

import com.digitalistgroup.springbootcodingassignment.data.ExchangeRateEntity;
import com.digitalistgroup.springbootcodingassignment.data.ExchangeRateRepository;
import com.digitalistgroup.springbootcodingassignment.model.BaseCurrency;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    public static final String API_KEY_HEADER_NAME = "apiKey";
    private final Environment environment;

    private final RestTemplate restTemplate;

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public ExchangeAmountResponseModel exchangeCurrency(ExchangeAmountRequestModel exchangeAmountRequestModel) {
        return new ExchangeAmountResponseModel(
                "EUR", "SEK", BigDecimal.valueOf(143.3890d), BigDecimal.valueOf(1509.34d)
        );
    }

    @Override
    public BaseCurrency getExchangeRates() {
        String exchangeRatesServiceUrl = environment.getProperty("exchange.rates.url");

        HttpEntity<String> entity = createHttpHeaders();

        assert exchangeRatesServiceUrl != null;
        return restTemplate.exchange(
                exchangeRatesServiceUrl, HttpMethod.GET, entity, BaseCurrency.class
        ).getBody();

    }

    @Override
    public void storeExchangeRates() {
        BaseCurrency exchangeRates = getExchangeRates();

        String baseCurrency = exchangeRates.getBaseCurrency();
        for (String exchangeCurrency : exchangeRates.getExchangeRates().keySet()) {
            ExchangeRateEntity exchangeRate =
                    exchangeRateRepository.findByBaseCurrencyAndExchangeCurrency(baseCurrency, exchangeCurrency);
            if (exchangeRate == null) {
                exchangeRate = new ExchangeRateEntity();
            }
            exchangeRate.setBaseCurrency(baseCurrency);
            exchangeRate.setExchangeCurrency(exchangeCurrency);
            exchangeRate.setExchangeRate(exchangeRates.getExchangeRates().get(exchangeCurrency));

            exchangeRateRepository.save(exchangeRate);
        }
    }

    private HttpEntity<String> createHttpHeaders() {
        String exchangeRatesServiceApiKey = environment.getProperty("exchange.rates.apiKey");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(API_KEY_HEADER_NAME, exchangeRatesServiceApiKey);

        return new HttpEntity<>(headers);
    }
}
