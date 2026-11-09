package com.carrental.carrentalsystem.carmanagement.presentationlayer.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDTO {
    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private BigDecimal dailyRate;
    private Boolean isAvailable;
}
