package com.digitalistgroup.springbootcodingassignment.exception;

public class ExchangeRatesNotInitializedException extends RuntimeException {

    public ExchangeRatesNotInitializedException(String message) {
        super(message);
    }
}
