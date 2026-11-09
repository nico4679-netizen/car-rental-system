package com.carrental.carrentalsystem.carmanagement.presentationlayer;

import com.carrental.carrentalsystem.carmanagement.businesslayer.CarService;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.requests.CarRequestDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarResponseDTO;
import com.carrental.carrentalsystem.carmanagement.presentationlayer.responses.CarWithRentalsResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    /** Optional filters: ?make=Toyota&available=true */
    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> getAllCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) Boolean available) {
        return ResponseEntity.ok(carService.getAllCars(make, available));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> getCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    public ResponseEntity<CarResponseDTO> createCar(@Valid @RequestBody CarRequestDTO request) {
        CarResponseDTO created = carService.createCar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDTO> updateCar(@PathVariable Long id,
                                                    @Valid @RequestBody CarRequestDTO request) {
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    /** Aggregated: car + rental history. */
    @GetMapping("/{id}/rentals")
    public ResponseEntity<CarWithRentalsResponseDTO> getCarRentals(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarWithRentals(id));
    }
}
