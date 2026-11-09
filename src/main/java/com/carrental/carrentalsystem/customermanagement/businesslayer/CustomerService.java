package com.carrental.carrentalsystem.customermanagement.businesslayer;

import com.carrental.carrentalsystem.customermanagement.presentationlayer.requests.CustomerRequestDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerResponseDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerWithRentalsResponseDTO;


import java.util.List;

public interface CustomerService {
    List<CustomerResponseDTO> getAllCustomers(String lastName);
    CustomerResponseDTO getCustomerById(Long id);
    CustomerResponseDTO createCustomer(CustomerRequestDTO request);
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request);
    void deleteCustomer(Long id);
    CustomerWithRentalsResponseDTO getCustomerWithRentals(Long id);
}
