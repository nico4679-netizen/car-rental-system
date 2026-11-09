package com.carrental.carrentalsystem.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/** Validation error body: { timestamp, status, error, errors: { field: message } } */
@Getter
@AllArgsConstructor
public class ValidationErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String, String> errors;
}
