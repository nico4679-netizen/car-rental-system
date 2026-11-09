package com.carrental.carrentalsystem.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Full stack against the seeded H2 database. Each test is rolled back. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarRentalApiIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Test
    void seedData_hasAtLeastTenRecordsPerTable() throws Exception {
        mockMvc.perform(get("/api/v1/cars")).andExpect(jsonPath("$.length()", greaterThanOrEqualTo(10)));
        mockMvc.perform(get("/api/v1/customers")).andExpect(jsonPath("$.length()", greaterThanOrEqualTo(10)));
        mockMvc.perform(get("/api/v1/rentals")).andExpect(jsonPath("$.length()", greaterThanOrEqualTo(10)));
    }

    @Test
    void filterAvailableCars_onlyReturnsAvailable() throws Exception {
        mockMvc.perform(get("/api/v1/cars?available=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].isAvailable", everyItem(is(true))));
    }

    @Test
    void createRental_endToEnd_marksCarUnavailable() throws Exception {
        LocalDate start = LocalDate.now().plusDays(1);
        String json = """
                {"carId":6,"customerId":12,"startDate":"%s","endDate":"%s"}""".formatted(start, start.plusDays(4));

        mockMvc.perform(post("/api/v1/rentals").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.totalCost").value(260.00))
                .andExpect(jsonPath("$.car.isAvailable").value(false));

        // same car again -> 409
        mockMvc.perform(post("/api/v1/rentals").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void rentalForUnavailableSeedCar_returns409() throws Exception {
        LocalDate start = LocalDate.now().plusDays(1);
        String json = """
                {"carId":1,"customerId":2,"startDate":"%s","endDate":"%s"}""".formatted(start, start.plusDays(2));
        mockMvc.perform(post("/api/v1/rentals").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Car 1 is not available for rental"));
    }

    @Test
    void invalidRental_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/rentals").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.carId").exists())
                .andExpect(jsonPath("$.errors.startDate").exists());
    }

    @Test
    void aggregatedEndpoints_returnNestedRentals() throws Exception {
        mockMvc.perform(get("/api/v1/cars/1/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.car.make").value("Toyota"))
                .andExpect(jsonPath("$.rentals.length()").value(2));
        mockMvc.perform(get("/api/v1/customers/1/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.rentals.length()").value(2));
    }

    @Test
    void missingResources_return404() throws Exception {
        mockMvc.perform(get("/api/v1/cars/9999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/v1/customers/9999/rentals")).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/v1/rentals/9999")).andExpect(status().isNotFound());
    }

    @Test
    void deleteCarWithHistory_returns409_andDeleteNewCar_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/1")).andExpect(status().isConflict());
        mockMvc.perform(delete("/api/v1/cars/12")).andExpect(status().isNoContent()); // car 12 has no rentals
    }

    @Test
    void returnRental_freesCar() throws Exception {
        mockMvc.perform(post("/api/v1/rentals/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.car.isAvailable").value(true));
    }
}
