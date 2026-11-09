package com.carrental.carrentalsystem.carmanagement.businesslayer;

import com.carrental.carrentalsystem.carmanagement.dataaccesslayer.CarRepository;
import com.carrental.carrentalsystem.carmanagement.domainlayer.Car;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.requests.CarRequestDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarResponseDTO;
import com.carrental.carrentalsystem.common.exception.DuplicateResourceException;
import com.carrental.carrentalsystem.common.exception.ResourceInUseException;
import com.carrental.carrentalsystem.common.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.customermanagement.businesslayer.CustomerMapper;
import com.carrental.carrentalsystem.rentalmanagement.businesslayer.RentalMapper;
import com.carrental.carrentalsystem.rentalmanagement.dataaccesslayer.RentalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock CarRepository carRepository;
    @Mock RentalRepository rentalRepository;

    CarServiceImpl service;

    @BeforeEach
    void setUp() {
        CarMapper carMapper = new CarMapper();
        service = new CarServiceImpl(carRepository, rentalRepository, carMapper,
                new RentalMapper(carMapper, new CustomerMapper()));
    }

    private CarRequestDTO request() {
        return new CarRequestDTO("Toyota", "Camry", 2024, "ABC-1234", new BigDecimal("55.00"), null);
    }

    @Test
    void getCarById_missing_throwsNotFound() {
        when(carRepository.findById(123L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> service.getCarById(123L));
        assertEquals("Car 123 not found", ex.getMessage());
    }

    @Test
    void createCar_defaultsToAvailable() {
        when(carRepository.existsByLicensePlateValue("ABC-1234")).thenReturn(false);
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> {
            Car c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        CarResponseDTO result = service.createCar(request());

        assertEquals(1L, result.getId());
        assertTrue(result.getIsAvailable());
    }

    @Test
    void createCar_duplicatePlate_throws() {
        when(carRepository.existsByLicensePlateValue("ABC-1234")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.createCar(request()));
        verify(carRepository, never()).save(any());
    }

    @Test
    void deleteCar_withRentalHistory_throwsInUse() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(new Car()));
        when(rentalRepository.existsByCar_Id(1L)).thenReturn(true);
        assertThrows(ResourceInUseException.class, () -> service.deleteCar(1L));
        verify(carRepository, never()).delete(any());
    }
}
