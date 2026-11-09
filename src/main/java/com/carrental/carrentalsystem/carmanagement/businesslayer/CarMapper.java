package com.carrental.carrentalsystem.carmanagement.businesslayer;

import com.carrental.carrentalsystem.carmanagement.domainlayer.*;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.requests.CarRequestDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public Car toEntity(CarRequestDTO dto) {
        Car car = new Car();
        updateEntity(car, dto);
        return car;
    }

    /** Copies request fields onto the entity; availability only changes when supplied. */
    public void updateEntity(Car car, CarRequestDTO dto) {
        car.setCarModel(new CarModel(dto.getMake().trim(), dto.getModel().trim()));
        car.setModelYear(new ModelYear(dto.getYear()));
        car.setLicensePlate(new LicensePlate(dto.getLicensePlate().trim()));
        car.setDailyRate(new DailyRate(dto.getDailyRate()));
        if (dto.getIsAvailable() != null) {
            car.setIsAvailable(dto.getIsAvailable());
        }
    }

    public CarResponseDTO toResponse(Car car) {
        return new CarResponseDTO(car.getId(),
                car.getCarModel().getMake(), car.getCarModel().getModel(),
                car.getModelYear().getValue(), car.getLicensePlate().getValue(),
                car.getDailyRate().getValue(), car.getIsAvailable());
    }
}
