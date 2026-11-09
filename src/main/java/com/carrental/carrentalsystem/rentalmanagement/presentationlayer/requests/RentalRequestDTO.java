package com.carrental.carrentalsystem.rentalmanagement.presentationlayer.requests;

import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestDTO {
    @NotNull(message = "carId is required")
    private Long carId;

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    /** Optional. Ignored on creation (always ACTIVE); may be set on PUT. */
    private RentalStatus status;
}
