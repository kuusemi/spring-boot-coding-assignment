package com.digitalistgroup.springbootcodingassignment.scheduler;

import com.digitalistgroup.springbootcodingassignment.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRatesPollTask {

    private final CurrencyExchangeService exchangeService;

    public void initializeExchangeRates() {
        log.info("Initializing the exchange rates...");
        exchangeService.storeExchangeRates();
        log.info("Finished the initialization of exchange rates");
    }

    public void updateExchangeRates() {
        log.info("Starting the exchange rate update");
        exchangeService.storeExchangeRates();
        log.info("Finished the exchange rate update");
    }





}
