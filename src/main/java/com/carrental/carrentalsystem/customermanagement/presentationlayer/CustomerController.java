package com.carrental.carrentalsystem.customermanagement.presentationlayer;

import com.carrental.carrentalsystem.customermanagement.businesslayer.CustomerService;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.requests.CustomerRequestDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerResponseDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerWithRentalsResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /** Optional filter: ?lastName=Doe */
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(
            @RequestParam(required = false) String lastName) {
        return ResponseEntity.ok(customerService.getAllCustomers(lastName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO created = customerService.createCustomer(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id,
                                                              @Valid @RequestBody CustomerRequestDTO request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    /** Aggregated: customer + all their rentals. */
    @GetMapping("/{id}/rentals")
    public ResponseEntity<CustomerWithRentalsResponseDTO> getCustomerRentals(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerWithRentals(id));
    }
}
