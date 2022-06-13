package com.digitalistgroup.springbootcodingassignment.service;

import com.digitalistgroup.springbootcodingassignment.data.ExchangeRateEntity;
import com.digitalistgroup.springbootcodingassignment.data.ExchangeRateRepository;
import com.digitalistgroup.springbootcodingassignment.exception.ExchangeRatesNotInitializedException;
import com.digitalistgroup.springbootcodingassignment.model.BaseCurrency;

import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private ResponseEntity<BaseCurrency> responseEntity;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private CurrencyExchangeServiceImpl service;

    @Test
    public void shouldGetExchangeRatesFromExternalService() {
        BaseCurrency exchangeRates = createCurrencyExchangeRates();

        // given
        given(environment.getProperty("exchange.rates.url"))
                .willReturn(
                        "https://api.apilayer.com/exchangerates_data/latest?symbols=EUR,USD,SEK&base=EUR");
        given(environment.getProperty("exchange.rates.apiKey")).willReturn("validApiKey");

        given(restTemplate.exchange(
                anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<BaseCurrency>>any())
        ).willReturn(responseEntity);

        given(responseEntity.getBody()).willReturn(exchangeRates);

        // when
        BaseCurrency returnValue = service.getExchangeRates();

        // then
        verify(environment, times(2)).getProperty(anyString());
        verify(restTemplate, times(1)).exchange(
                anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<BaseCurrency>>any()
        );
        verify(responseEntity, times(1)).getBody();

        assertThat(returnValue).isEqualTo(exchangeRates);
    }

    @Test
    public void shouldGetExchangeRatesFromExternalServiceAndCreateThemWhenNotExisting() {
        BaseCurrency exchangeRates = createCurrencyExchangeRates();
        // given
        given(environment.getProperty("exchange.rates.url"))
                .willReturn(
                        "https://api.apilayer.com/exchangerates_data/latest?symbols=EUR,USD,SEK&base=EUR");
        given(environment.getProperty("exchange.rates.apiKey")).willReturn("validApiKey");

        given(restTemplate.exchange(
                anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<BaseCurrency>>any())
        ).willReturn(responseEntity);

        given(responseEntity.getBody()).willReturn(exchangeRates);

        given(
                exchangeRateRepository.findByBaseCurrencyAndExchangeCurrency(anyString(), anyString())
        ).willReturn(null);

        // when
        service.storeExchangeRates();

        // then
        verify(environment, times(2)).getProperty(anyString());
        verify(restTemplate, times(1)).exchange(
                anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<BaseCurrency>>any()
        );
        verify(responseEntity, times(1)).getBody();
        verify(exchangeRateRepository,
                times(3)).findByBaseCurrencyAndExchangeCurrency(anyString(), anyString());
        verify(exchangeRateRepository, times(3)).save(any(ExchangeRateEntity.class));

    }

    @Test()
    public void shouldReturnAnErrorWhenExchangeRatesHaveNotBeenInitialized() {
        // given
        ExchangeAmountRequestModel fromCurrency =
                new ExchangeAmountRequestModel("SEK", "USD", BigDecimal.valueOf(104.90d));

        given(exchangeRateRepository.findByBaseCurrencyAndExchangeCurrency(
                anyString(), anyString())
        ).willReturn(null);

        // when
        assertThatThrownBy(() -> service.exchangeCurrency(fromCurrency))
                .isInstanceOf(ExchangeRatesNotInitializedException.class);
    }

    @Test
    public void shouldReturnAValidAnswerWhenCurrencyIsNotExchangedFromBaseCurrency() {
        // given
        ExchangeAmountRequestModel fromCurrency =
                new ExchangeAmountRequestModel("SEK", "USD", BigDecimal.valueOf(104.90d));

        ExchangeRateEntity fromEntity =
                createExchangeRateEntity("EUR", "SEK", 10.602037d);
        ExchangeRateEntity toEntity =
                createExchangeRateEntity("EUR", "USD", 1.046627d);

        given(exchangeRateRepository.findByBaseCurrencyAndExchangeCurrency(anyString(), anyString())).willReturn(
                fromEntity, toEntity
        );

        ExchangeAmountResponseModel response = service.exchangeCurrency(fromCurrency);

        verify(exchangeRateRepository, times(2))
                .findByBaseCurrencyAndExchangeCurrency(anyString(), anyString());

        assertThat(response.getToAmount()).isEqualTo(BigDecimal.valueOf(10.3557d));
    }

    private ExchangeRateEntity createExchangeRateEntity(String fromCurrency, String toCurrency, double exchangeRate) {
        ExchangeRateEntity entity = new ExchangeRateEntity();
        entity.setBaseCurrency(fromCurrency);
        entity.setExchangeCurrency(toCurrency);
        entity.setExchangeRate(BigDecimal.valueOf(exchangeRate));

        return entity;
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
