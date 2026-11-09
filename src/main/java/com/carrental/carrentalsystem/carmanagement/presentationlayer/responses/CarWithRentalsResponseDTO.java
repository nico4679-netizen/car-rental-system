package com.carrental.carrentalsystem.carmanagement.presentationlayer.responses;

import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Aggregated DTO #1: Car + its rental history. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarWithRentalsResponseDTO {
    private CarResponseDTO car;
    private List<RentalSummaryDTO> rentals;
}
