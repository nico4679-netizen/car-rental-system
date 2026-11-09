package com.carrental.carrentalsystem.customermanagement.businesslayer;

import com.carrental.carrentalsystem.customermanagement.domainlayer.*;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.requests.CustomerRequestDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        updateEntity(customer, dto);
        return customer;
    }

    public void updateEntity(Customer customer, CustomerRequestDTO dto) {
        customer.setName(new CustomerName(dto.getFirstName().trim(), dto.getLastName().trim()));
        customer.setEmail(new EmailAddress(dto.getEmail().trim()));
        customer.setPhoneNumber(new PhoneNumber(dto.getPhoneNumber()));
        customer.setDriverLicenseNumber(new DriverLicenseNumber(dto.getDriverLicenseNumber().trim()));
    }

    public CustomerResponseDTO toResponse(Customer c) {
        return new CustomerResponseDTO(c.getId(),
                c.getName().getFirstName(), c.getName().getLastName(),
                c.getEmail().getValue(),
                c.getPhoneNumber() == null ? null : c.getPhoneNumber().getValue(),
                c.getDriverLicenseNumber().getValue());
    }
}
