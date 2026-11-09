package com.carrental.carrentalsystem.carmanagement.businesslayer;

import com.carrental.carrentalsystem.carmanagement.dataaccesslayer.CarRepository;
import com.carrental.carrentalsystem.carmanagement.domainlayer.Car;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.requests.CarRequestDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarResponseDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarWithRentalsResponseDTO;
import com.carrental.carrentalsystem.common.exception.DuplicateResourceException;
import com.carrental.carrentalsystem.common.exception.ResourceInUseException;
import com.carrental.carrentalsystem.common.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.rentalmanagement.businesslayer.RentalMapper;
import com.carrental.carrentalsystem.rentalmanagement.dataaccesslayer.RentalRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;
    private final CarMapper carMapper;
    private final RentalMapper rentalMapper;

    @Override
    public List<CarResponseDTO> getAllCars(String make, Boolean available) {
        List<Car> cars;
        boolean hasMake = make != null && !make.isBlank();
        if (hasMake && available != null) {
            cars = carRepository.findByCarModelMakeIgnoreCaseAndIsAvailable(make, available);
        } else if (hasMake) {
            cars = carRepository.findByCarModelMakeIgnoreCase(make);
        } else if (available != null) {
            cars = carRepository.findByIsAvailable(available);
        } else {
            cars = carRepository.findAll();
        }
        return cars.stream().map(carMapper::toResponse).toList();
    }

    @Override
    public CarResponseDTO getCarById(Long id) {
        return carMapper.toResponse(findCar(id));
    }

    @Override
    @Transactional
    public CarResponseDTO createCar(CarRequestDTO request) {
        if (carRepository.existsByLicensePlateValue(request.getLicensePlate().trim())) {
            throw new DuplicateResourceException(
                    "A car with license plate " + request.getLicensePlate() + " already exists");
        }
        Car car = carMapper.toEntity(request);
        return carMapper.toResponse(carRepository.save(car));
    }

    @Override
    @Transactional
    public CarResponseDTO updateCar(Long id, CarRequestDTO request) {
        Car car = findCar(id);
        if (carRepository.existsByLicensePlateValueAndIdNot(request.getLicensePlate().trim(), id)) {
            throw new DuplicateResourceException(
                    "A car with license plate " + request.getLicensePlate() + " already exists");
        }
        carMapper.updateEntity(car, request);
        return carMapper.toResponse(carRepository.save(car));
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        Car car = findCar(id);
        if (rentalRepository.existsByCar_Id(id)) {
            throw new ResourceInUseException(
                    "Car " + id + " has rental history and cannot be deleted");
        }
        carRepository.delete(car);
    }

    @Override
    public CarWithRentalsResponseDTO getCarWithRentals(Long id) {
        Car car = findCar(id);
        return new CarWithRentalsResponseDTO(
                carMapper.toResponse(car),
                rentalRepository.findByCar_Id(id).stream().map(rentalMapper::toSummary).toList());
    }

    private Car findCar(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
    }
}
