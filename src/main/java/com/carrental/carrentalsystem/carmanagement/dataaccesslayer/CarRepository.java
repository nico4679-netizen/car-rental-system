package com.carrental.carrentalsystem.carmanagement.dataaccesslayer;

import com.carrental.carrentalsystem.carmanagement.domainlayer.Car;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByLicensePlateValue(String licensePlate);
    boolean existsByLicensePlateValueAndIdNot(String licensePlate, Long id);
    List<Car> findByIsAvailable(Boolean isAvailable);
    List<Car> findByCarModelMakeIgnoreCase(String make);
    List<Car> findByCarModelMakeIgnoreCaseAndIsAvailable(String make, Boolean isAvailable);
}
