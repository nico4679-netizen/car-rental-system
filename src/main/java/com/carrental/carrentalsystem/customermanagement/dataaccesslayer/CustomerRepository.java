package com.carrental.carrentalsystem.customermanagement.dataaccesslayer;

import com.carrental.carrentalsystem.customermanagement.domainlayer.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmailValue(String email);
    boolean existsByEmailValueAndIdNot(String email, Long id);
    boolean existsByDriverLicenseNumberValue(String driverLicenseNumber);
    boolean existsByDriverLicenseNumberValueAndIdNot(String driverLicenseNumber, Long id);
    List<Customer> findByNameLastNameIgnoreCase(String lastName);
}
