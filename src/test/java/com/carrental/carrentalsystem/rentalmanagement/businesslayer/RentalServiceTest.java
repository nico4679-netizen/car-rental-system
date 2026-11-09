package com.carrental.carrentalsystem.rentalmanagement.businesslayer;

import com.carrental.carrentalsystem.carmanagement.businesslayer.CarMapper;
import com.carrental.carrentalsystem.carmanagement.dataaccesslayer.CarRepository;
import com.carrental.carrentalsystem.carmanagement.domainlayer.*;
import com.carrental.carrentalsystem.common.exception.CarNotAvailableException;
import com.carrental.carrentalsystem.common.exception.InvalidRentalException;
import com.carrental.carrentalsystem.common.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.customermanagement.businesslayer.CustomerMapper;
import com.carrental.carrentalsystem.customermanagement.dataaccesslayer.CustomerRepository;
import com.carrental.carrentalsystem.customermanagement.domainlayer.*;
import com.carrental.carrentalsystem.rentalmanagement.dataaccesslayer.RentalRepository;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.Money;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.Rental;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalPeriod;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.requests.RentalRequestDTO;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock RentalRepository rentalRepository;
    @Mock CarRepository carRepository;
    @Mock CustomerRepository customerRepository;

    RentalServiceImpl service;
    Car car;
    Customer customer;

    @BeforeEach
    void setUp() {
        RentalMapper mapper = new RentalMapper(new CarMapper(), new CustomerMapper());
        service = new RentalServiceImpl(rentalRepository, carRepository, customerRepository, mapper);

        car = new Car();
        car.setId(1L);
        car.setCarModel(new CarModel("Toyota", "Camry"));
        car.setModelYear(new ModelYear(2024));
        car.setLicensePlate(new LicensePlate("ABC-1234"));
        car.setDailyRate(new DailyRate(new BigDecimal("55.00")));
        car.setIsAvailable(true);

        customer = new Customer();
        customer.setId(2L);
        customer.setName(new CustomerName("John", "Doe"));
        customer.setEmail(new EmailAddress("john.doe@example.com"));
        customer.setDriverLicenseNumber(new DriverLicenseNumber("DL-98765432"));
    }

    private RentalRequestDTO request(LocalDate start, LocalDate end) {
        return new RentalRequestDTO(1L, 2L, start, end, null);
    }

    @Test
    void createRental_calculatesCost_andMarksCarUnavailable() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(inv -> {
            Rental r = inv.getArgument(0);
            r.setId(10L);
            return r;
        });

        LocalDate start = LocalDate.now().plusDays(1);
        RentalResponseDTO result = service.createRental(request(start, start.plusDays(4)));

        assertEquals(new BigDecimal("220.00"), result.getTotalCost());
        assertEquals(RentalStatus.ACTIVE, result.getStatus());
        assertFalse(car.getIsAvailable());
    }

    @Test
    void createRental_carNotAvailable_throws() {
        car.setIsAvailable(false);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));

        LocalDate start = LocalDate.now().plusDays(1);
        assertThrows(CarNotAvailableException.class,
                () -> service.createRental(request(start, start.plusDays(2))));
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void createRental_endBeforeStart_throwsInvalidRental() {
        LocalDate start = LocalDate.now().plusDays(5);
        assertThrows(InvalidRentalException.class,
                () -> service.createRental(request(start, start.minusDays(1))));
    }

    @Test
    void createRental_unknownCar_throwsNotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        LocalDate start = LocalDate.now().plusDays(1);
        assertThrows(ResourceNotFoundException.class,
                () -> service.createRental(request(start, start.plusDays(2))));
    }

    @Test
    void returnRental_completesRental_andFreesCar() {
        car.setIsAvailable(false);
        Rental rental = new Rental();
        rental.setId(10L);
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setRentalPeriod(new RentalPeriod(LocalDate.now(), LocalDate.now().plusDays(2)));
        rental.setTotalCost(new Money(new BigDecimal("110.00")));
        rental.setStatus(RentalStatus.ACTIVE);
        when(rentalRepository.findById(10L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental)).thenReturn(rental);

        RentalResponseDTO result = service.returnRental(10L);

        assertEquals(RentalStatus.COMPLETED, result.getStatus());
        assertTrue(car.getIsAvailable());
    }

    @Test
    void deleteRental_missing_throwsNotFound() {
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteRental(99L));
    }
}
