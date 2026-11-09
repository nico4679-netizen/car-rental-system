package com.carrental.carrentalsystem.carmanagement.presentationlayer;

import com.carrental.carrentalsystem.carmanagement.businesslayer.CarService;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarResponseDTO;
import com.carrental.carrentalsystem.common.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean CarService carService;

    private CarResponseDTO sample() {
        return new CarResponseDTO(1L, "Toyota", "Camry", 2024, "ABC-1234", new BigDecimal("55.00"), true);
    }

    @Test
    void getAllCars_returns200() throws Exception {
        when(carService.getAllCars(null, null)).thenReturn(List.of(sample()));
        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[0].isAvailable").value(true));
    }

    @Test
    void getCar_missing_returns404WithErrorBody() throws Exception {
        when(carService.getCarById(123L)).thenThrow(new ResourceNotFoundException("Car", 123L));
        mockMvc.perform(get("/api/v1/cars/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Car 123 not found"))
                .andExpect(jsonPath("$.path").value("/api/v1/cars/123"));
    }

    @Test
    void createCar_valid_returns201WithLocation() throws Exception {
        when(carService.createCar(any())).thenReturn(sample());
        String json = """
                {"make":"Toyota","model":"Camry","year":2024,"licensePlate":"ABC-1234","dailyRate":55.00}""";
        mockMvc.perform(post("/api/v1/cars").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/cars/1")))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createCar_invalid_returns400WithFieldErrors() throws Exception {
        String json = """
                {"make":"","model":"Camry","year":2024,"licensePlate":"ABC-1234","dailyRate":-5}""";
        mockMvc.perform(post("/api/v1/cars").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.make").value("Make cannot be blank"))
                .andExpect(jsonPath("$.errors.dailyRate").value("Daily rate must be positive"));
    }

    @Test
    void deleteCar_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/1")).andExpect(status().isNoContent());
    }
}
