package com.carrental.carrentalsystem.rentalmanagement.businesslayer;

import com.carrental.carrentalsystem.carmanagement.businesslayer.CarMapper;
import com.carrental.carrentalsystem.customermanagement.businesslayer.CustomerMapper;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.Rental;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalResponseDTO;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalSummaryDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalMapper {
    private final CarMapper carMapper;
    private final CustomerMapper customerMapper;

    public RentalResponseDTO toResponse(Rental r) {
        return new RentalResponseDTO(r.getId(),
                carMapper.toResponse(r.getCar()),
                customerMapper.toResponse(r.getCustomer()),
                r.getRentalPeriod().getStartDate(), r.getRentalPeriod().getEndDate(),
                r.getTotalCost().getValue(), r.getStatus());
    }

    public RentalSummaryDTO toSummary(Rental r) {
        return new RentalSummaryDTO(r.getId(), r.getCar().getId(), r.getCustomer().getId(),
                r.getRentalPeriod().getStartDate(), r.getRentalPeriod().getEndDate(),
                r.getTotalCost().getValue(), r.getStatus());
    }
}
