package com.digitalistgroup.springbootcodingassignment.service;

import com.digitalistgroup.springbootcodingassignment.model.BaseCurrency;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountRequestModel;
import com.digitalistgroup.springbootcodingassignment.model.ExchangeAmountResponseModel;

public interface CurrencyExchangeService {
    ExchangeAmountResponseModel exchangeCurrency(ExchangeAmountRequestModel exchangeAmountRequestModel);

    BaseCurrency getExchangeRates();

    void storeExchangeRates();
}
