package com.carrental.carrentalsystem.carmanagement.domainlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyRate {
    @Column(name = "daily_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    /** Cost of renting at this rate for the given number of days. */
    public BigDecimal costFor(long days) {
        return value.multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_UP);
    }
}
