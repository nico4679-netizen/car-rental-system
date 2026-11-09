package com.carrental.carrentalsystem.rentalmanagement.presentationlayer;

import com.carrental.carrentalsystem.rentalmanagement.businesslayer.RentalService;
import com.carrental.carrentalsystem.rentalmanagement.domainlayer.RentalStatus;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.requests.RentalRequestDTO;
import com.carrental.carrentalsystem.rentalmanagement.presentationlayer.responses.RentalResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    /** Optional filter: ?status=ACTIVE */
    @GetMapping
    public ResponseEntity<List<RentalResponseDTO>> getAllRentals(
            @RequestParam(required = false) RentalStatus status) {
        return ResponseEntity.ok(rentalService.getAllRentals(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> getRental(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(@Valid @RequestBody RentalRequestDTO request) {
        RentalResponseDTO created = rentalService.createRental(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> updateRental(@PathVariable Long id,
                                                          @Valid @RequestBody RentalRequestDTO request) {
        return ResponseEntity.ok(rentalService.updateRental(id, request));
    }

    /** Returns the car: marks the rental COMPLETED and frees the car. */
    @PostMapping("/{id}/return")
    public ResponseEntity<RentalResponseDTO> returnRental(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.returnRental(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }
}
