package com.digitalistgroup.springbootcodingassignment.controller;

import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;
import com.digitalistgroup.springbootcodingassignment.service.CurrencyExchangeService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeControllerTest {

    private MockMvc mvc;

    @Mock
    private CurrencyExchangeService exchangeService;

    @InjectMocks
    private CurrencyExchangeController controller;

    private JacksonTester<ExchangeAmountResponseModel> responseTester;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @ParameterizedTest
    @MethodSource("invalidCurrencyExchangeRequestsToValidate")
    public void shouldReturn400BadRequestForAnInvalidRequest(
            String from, String to, BigDecimal fromAmount) throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("from", from);
        params.set("to", to);
        params.set("from_amount", fromAmount!= null ? fromAmount.toString() : null);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/exchange_amount")
                                .params(params)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnValidResultOfCurrencyExchange() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("from", "SEK");
        params.set("to", "EUR");
        params.set("from_amount", Double.toString(10.602037d));

        ExchangeAmountResponseModel responseModel = new ExchangeAmountResponseModel(
                "SEK", "EUR", BigDecimal.valueOf(0.27d), BigDecimal.valueOf(1.0d)
        );

        given(exchangeService.exchangeCurrency(
                any(ExchangeAmountRequestModel.class)
        )).willReturn(responseModel);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/exchange_amount")
                                .params(params)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                responseTester.write(responseModel).getJson()
        );

    }
    private static Stream<Arguments> invalidCurrencyExchangeRequestsToValidate() {
        return Stream.of(
                Arguments.of(null, "SEK", BigDecimal.valueOf(109.87d)),
                Arguments.of("USD", null, BigDecimal.valueOf(114.76d)),
                Arguments.of("SEK", "USD", null)
        );
    }

}
