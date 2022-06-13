package com.digitalistgroup.springbootcodingassignment.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ExchangeRatesPollScheduler {

    private final ExchangeRatesPollTask exchangeRatesPollTask;

    @PostConstruct
    private void initializeExchangeRates() {
        exchangeRatesPollTask.initializeExchangeRates();
    }

    @Scheduled(cron = "0 * * * *", zone = "Europe/Helsinki")
    public void updateExchangeRates() {
        exchangeRatesPollTask.updateExchangeRates();
    }
}
