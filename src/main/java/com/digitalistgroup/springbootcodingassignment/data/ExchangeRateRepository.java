package com.digitalistgroup.springbootcodingassignment.data;

import org.springframework.data.repository.CrudRepository;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRateEntity, Long> {
    ExchangeRateEntity findByBaseCurrencyAndExchangeCurrency(String baseCurrency, String exchangeCurrency);

}
