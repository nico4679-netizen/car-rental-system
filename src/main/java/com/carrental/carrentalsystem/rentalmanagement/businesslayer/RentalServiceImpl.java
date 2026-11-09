package com.carrental.carrentalsystem.rentalmanagement.businesslayer;

import com.carrental.carrentalsystem.carmanagement.dataaccesslayer.CarRepository;
import com.carrental.carrentalsystem.carmanagement.domainlayer.Car;
import com.carrental.carrentalsystem.common.exception.CarNotAvailableException;
import com.carrental.carrentalsystem.common.exception.InvalidRentalException;
import com.carrental.carrentalsystem.common.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.customermanagement.dataaccesslayer.CustomerRepository;
import com.carrental.carrentalsystem.customermanagement.domainlayer.Customer;
import com.carrental.carrentalsystem.rentalmanagement.dataaccesslayer.RentalRepository;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.Money;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.Rental;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalPeriod;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.requests.RentalRequestDTO;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Business rules:
 *  - a rental can only be created for an available car (else 409);
 *  - endDate must be after startDate (else 400);
 *  - totalCost = number of days x car daily rate;
 *  - an ACTIVE rental holds its car (isAvailable=false); completing,
 *    cancelling or deleting it releases the car.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final RentalMapper rentalMapper;

    @Override
    public List<RentalResponseDTO> getAllRentals(RentalStatus status) {
        List<Rental> rentals = (status != null)
                ? rentalRepository.findByStatus(status)
                : rentalRepository.findAll();
        return rentals.stream().map(rentalMapper::toResponse).toList();
    }

    @Override
    public RentalResponseDTO getRentalById(Long id) {
        return rentalMapper.toResponse(findRental(id));
    }

    @Override
    @Transactional
    public RentalResponseDTO createRental(RentalRequestDTO request) {
        validateDates(request.getStartDate(), request.getEndDate());
        Car car = findCar(request.getCarId());
        Customer customer = findCustomer(request.getCustomerId());
        if (!Boolean.TRUE.equals(car.getIsAvailable())) {
            throw new CarNotAvailableException(car.getId());
        }

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setRentalPeriod(new RentalPeriod(request.getStartDate(), request.getEndDate()));
        rental.setTotalCost(calculateCost(car, rental.getRentalPeriod()));
        rental.setStatus(RentalStatus.ACTIVE);
        car.setIsAvailable(false);

        return rentalMapper.toResponse(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public RentalResponseDTO updateRental(Long id, RentalRequestDTO request) {
        Rental rental = findRental(id);
        validateDates(request.getStartDate(), request.getEndDate());
        Car newCar = findCar(request.getCarId());
        Customer customer = findCustomer(request.getCustomerId());

        RentalStatus oldStatus = rental.getStatus();
        RentalStatus newStatus = request.getStatus() != null ? request.getStatus() : oldStatus;

        // 1) release the old car if this rental was holding it
        if (oldStatus == RentalStatus.ACTIVE) {
            rental.getCar().setIsAvailable(true);
        }
        // 2) hold the (possibly new) car if the rental is ACTIVE afterwards.
        //    An exception here rolls the whole transaction back, including step 1.
        if (newStatus == RentalStatus.ACTIVE) {
            if (!Boolean.TRUE.equals(newCar.getIsAvailable())) {
                throw new CarNotAvailableException(newCar.getId());
            }
            newCar.setIsAvailable(false);
        }

        rental.setCar(newCar);
        rental.setCustomer(customer);
        rental.setRentalPeriod(new RentalPeriod(request.getStartDate(), request.getEndDate()));
        rental.setTotalCost(calculateCost(newCar, rental.getRentalPeriod()));
        rental.setStatus(newStatus);

        return rentalMapper.toResponse(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public RentalResponseDTO returnRental(Long id) {
        Rental rental = findRental(id);
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new InvalidRentalException("Rental " + id + " is not active (status "
                    + rental.getStatus() + ")");
        }
        rental.setStatus(RentalStatus.COMPLETED);
        rental.getCar().setIsAvailable(true);
        return rentalMapper.toResponse(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public void deleteRental(Long id) {
        Rental rental = findRental(id);
        if (rental.getStatus() == RentalStatus.ACTIVE) {
            rental.getCar().setIsAvailable(true);
        }
        rentalRepository.delete(rental);
    }

    // ---------- helpers
    private void validateDates(LocalDate start, LocalDate end) {
        if (!end.isAfter(start)) {
            throw new InvalidRentalException("End date must be after start date");
        }
    }

    private Money calculateCost(Car car, RentalPeriod period) {
        return new Money(car.getDailyRate().costFor(period.days()));
    }

    private Rental findRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", id));
    }

    private Car findCar(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }
}
