package com.digitalistgroup.springbootcodingassignment.service;

import com.digitalistgroup.springbootcodingassignment.model.BaseCurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeServiceImplTest {

    @Mock
    private Environment environment;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeServiceImpl service;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void shouldGetExchangeRatesFromExternalService() {
        BaseCurrency exchangeRates = createCurrencyExchangeRates();

        // given
        given(environment.getProperty("exchange.rates.url"))
                .willReturn(
                        "https://api.apilayer.com/exchangerates_data/latest?symbols=EUR,USD,SEK&base=EUR");
        given(environment.getProperty("exchange.rates.apiKey")).willReturn("validApiKey");

        // when
        service.storeExchangeRates();

        // then
        verify(environment, times(2)).getProperty(anyString());
    }

    private BaseCurrency createCurrencyExchangeRates() {
        Map<String, BigDecimal> exchangeRates = Map.ofEntries(
                Map.entry("EUR", BigDecimal.valueOf(1d)),
                Map.entry("USD", BigDecimal.valueOf(1.062406d)),
                Map.entry("SEK", BigDecimal.valueOf(10.518334d))
        );

        return new BaseCurrency("EUR", exchangeRates);
    }
}
