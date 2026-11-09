package com.carrental.carrentalsystem.customermanagement.domainlayer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A registered customer. Every attribute is a value object, like the library project. */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CustomerId customerId = new CustomerId();

    @Embedded
    private CustomerName name;

    @Embedded
    private EmailAddress email;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private DriverLicenseNumber driverLicenseNumber;

    // No CascadeType.REMOVE: rental history is never deleted with the customer.
}
