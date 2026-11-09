package com.carrental.carrentalsystem.carmanagement.businesslayer;

import com.carrental.carrentalsystem.carmanagement.presentationlayer.requests.CarRequestDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarResponseDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarWithRentalsResponseDTO;


import java.util.List;

public interface CarService {
    List<CarResponseDTO> getAllCars(String make, Boolean available);
    CarResponseDTO getCarById(Long id);
    CarResponseDTO createCar(CarRequestDTO request);
    CarResponseDTO updateCar(Long id, CarRequestDTO request);
    void deleteCar(Long id);
    CarWithRentalsResponseDTO getCarWithRentals(Long id);
}
