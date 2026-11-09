package com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses;

import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarResponseDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerResponseDTO;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Rental with its Car and Customer nested (the "many" side with both parents). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponseDTO {
    private Long id;
    private CarResponseDTO car;
    private CustomerResponseDTO customer;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private RentalStatus status;
}
