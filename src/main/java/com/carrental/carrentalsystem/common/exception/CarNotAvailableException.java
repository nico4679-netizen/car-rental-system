package com.carrental.carrentalsystem.common.exception;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException(Long carId) {
        super("Car " + carId + " is not available for rental");
    }
}
