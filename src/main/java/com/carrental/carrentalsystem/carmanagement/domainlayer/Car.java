package com.carrental.carrentalsystem.carmanagement.domainlayer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A vehicle in the rental fleet. Every attribute is a value object, like the library project. */
@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CarId carId = new CarId();

    @Embedded
    private CarModel carModel;

    @Embedded
    private ModelYear modelYear;

    @Embedded
    private LicensePlate licensePlate;

    @Embedded
    private DailyRate dailyRate;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    // No collection of rentals and no CascadeType.REMOVE on purpose:
    // deleting a car must never silently delete rental history.
}
