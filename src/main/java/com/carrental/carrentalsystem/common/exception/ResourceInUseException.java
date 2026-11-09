package com.carrental.carrentalsystem.common.exception;

/** Thrown when deleting a car/customer that still has rental history. */
public class ResourceInUseException extends RuntimeException {
    public ResourceInUseException(String message) {
        super(message);
    }
}
