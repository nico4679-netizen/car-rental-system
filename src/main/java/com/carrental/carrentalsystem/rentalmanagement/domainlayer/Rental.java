package com.carrental.carrentalsystem.rentalmanagement.domainlayer;

import com.carrental.carrentalsystem.carmanagement.domainlayer.Car;
import com.carrental.carrentalsystem.customermanagement.domainlayer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Associative entity linking a Customer and a Car for a period of time. */
@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private RentalId rentalId = new RentalId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Embedded
    private RentalPeriod rentalPeriod;

    @Embedded
    private Money totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status = RentalStatus.ACTIVE;
}
