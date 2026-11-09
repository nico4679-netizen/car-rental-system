package com.carrental.carrentalsystem.rentalmanagement.domainlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Monetary amount (the rental's total cost). */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Money {
    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal value;
}
