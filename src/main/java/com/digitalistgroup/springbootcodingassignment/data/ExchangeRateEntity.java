package com.digitalistgroup.springbootcodingassignment.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exchange_rates")
public class ExchangeRateEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 10)
    private String baseCurrency;

    @Column(nullable = false, length = 10)
    private String exchangeCurrency;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal exchangeRate;
}
