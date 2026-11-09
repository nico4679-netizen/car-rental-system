package com.carrental.carrentalsystem.customermanagement.presentationlayer.responses;

import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Aggregated DTO #2: Customer + all their rentals. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWithRentalsResponseDTO {
    private CustomerResponseDTO customer;
    private List<RentalSummaryDTO> rentals;
}
