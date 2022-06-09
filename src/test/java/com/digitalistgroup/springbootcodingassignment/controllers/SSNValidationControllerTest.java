package com.digitalistgroup.springbootcodingassignment.controllers;

import com.digitalistgroup.springbootcodingassignment.models.ValidateSSNRequestModel;
import com.digitalistgroup.springbootcodingassignment.models.ValidateSSNResponseModel;
import com.digitalistgroup.springbootcodingassignment.validation.SSNValidationHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class SSNValidationControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private SSNValidationController controller;

    private JacksonTester<ValidateSSNRequestModel> requestTester;

    private JacksonTester<ValidateSSNResponseModel> responseTester;

    @BeforeEach
    public void setuo() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new SSNValidationHandler())
                .build();
    }

    @ParameterizedTest
    @MethodSource("invalidSSNRequestsToValidate")
    public void shouldReturn200OkAndFalseWhenSSNRequestInvalid(String ssn, String countryCode, boolean expected) throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/validate_ssn/").contentType(MediaType.APPLICATION_JSON_VALUE).content(
                        requestTester.write(new ValidateSSNRequestModel(ssn, countryCode)).getJson()
                )
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                responseTester.write(new ValidateSSNResponseModel(expected)).getJson()
        );
    }

    private static Stream<Arguments> invalidSSNRequestsToValidate() {
        return Stream.of(
                Arguments.of("131052-308", "FI", false),
                Arguments.of("131052-308T", "SE", false)
        );
    }

}
