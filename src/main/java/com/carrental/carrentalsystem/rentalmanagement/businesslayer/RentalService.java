package com.carrental.carrentalsystem.rentalmanagement.businesslayer;

import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.requests.RentalRequestDTO;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalResponseDTO;


import java.util.List;

public interface RentalService {
    List<RentalResponseDTO> getAllRentals(RentalStatus status);
    RentalResponseDTO getRentalById(Long id);
    RentalResponseDTO createRental(RentalRequestDTO request);
    RentalResponseDTO updateRental(Long id, RentalRequestDTO request);
    RentalResponseDTO returnRental(Long id);
    void deleteRental(Long id);
}
