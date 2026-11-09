package com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses;

import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Compact rental used inside aggregated responses (avoids repeating the parent). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalSummaryDTO {
    private Long id;
    private Long carId;
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private RentalStatus status;
}
