package com.carrental.carrentalsystem.rentalmanagement.dataaccesslayer;

import com.carrental.carrentalsystem.rentalmanagement.domainlayer.Rental;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByCar_Id(Long carId);
    List<Rental> findByCustomer_Id(Long customerId);
    List<Rental> findByStatus(RentalStatus status);
    boolean existsByCar_Id(Long carId);
    boolean existsByCustomer_Id(Long customerId);
}
