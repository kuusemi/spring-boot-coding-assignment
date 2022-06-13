package com.digitalistgroup.springbootcodingassignment.service;

import com.digitalistgroup.springbootcodingassignment.data.ExchangeRateEntity;
import com.digitalistgroup.springbootcodingassignment.data.ExchangeRateRepository;
import com.digitalistgroup.springbootcodingassignment.exception.ExchangeRatesNotInitializedException;
import com.digitalistgroup.springbootcodingassignment.model.BaseCurrency;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    public static final String API_KEY_HEADER_NAME = "apiKey";
    private static final String BASE_CURRENCY = "EUR";
    private final Environment environment;

    private final RestTemplate restTemplate;

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public ExchangeAmountResponseModel exchangeCurrency(
            ExchangeAmountRequestModel exchangeAmountRequestModel) throws ExchangeRatesNotInitializedException {
        String fromCurrency = exchangeAmountRequestModel.getFrom();
        String toCurrency = exchangeAmountRequestModel.getTo();
        BigDecimal fromAmount = exchangeAmountRequestModel.getFromAmount();

        ExchangeRateEntity fromRate =
                exchangeRateRepository.findByBaseCurrencyAndExchangeCurrency(BASE_CURRENCY, fromCurrency);
        ExchangeRateEntity toRate =
                exchangeRateRepository.findByBaseCurrencyAndExchangeCurrency(BASE_CURRENCY, toCurrency);

        if (fromRate == null || toRate == null) {
            log.error("Exchange rates haven't been initialized properly");
            throw new ExchangeRatesNotInitializedException("Exchange rates haven't been initialized properly");
        }
        BigDecimal baseCurrencyAmount = fromAmount;
        if (!fromCurrency.equals(BASE_CURRENCY)) {
            baseCurrencyAmount = fromAmount.divide(
                    fromRate.getExchangeRate(), new MathContext(6, RoundingMode.HALF_UP));
        }
        BigDecimal toAmount = baseCurrencyAmount.multiply(
                toRate.getExchangeRate(), new MathContext(6, RoundingMode.HALF_UP));

        return new ExchangeAmountResponseModel(
                fromCurrency, toCurrency, toAmount, toRate.getExchangeRate()
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
