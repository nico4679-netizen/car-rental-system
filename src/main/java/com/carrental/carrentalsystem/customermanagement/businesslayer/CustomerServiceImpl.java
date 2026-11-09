package com.carrental.carrentalsystem.customermanagement.businesslayer;

import com.carrental.carrentalsystem.common.exception.DuplicateResourceException;
import com.carrental.carrentalsystem.common.exception.ResourceInUseException;
import com.carrental.carrentalsystem.common.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.customermanagement.dataaccesslayer.CustomerRepository;
import com.carrental.carrentalsystem.customermanagement.domainlayer.Customer;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.requests.CustomerRequestDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerResponseDTO;
import com.carrental.carrentalsystem.customermanagement.presentationlayer.responses.CustomerWithRentalsResponseDTO;
import com.carrental.carrentalsystem.rentalmanagement.businesslayer.RentalMapper;
import com.carrental.carrentalsystem.rentalmanagement.dataaccesslayer.RentalRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;
    private final CustomerMapper customerMapper;
    private final RentalMapper rentalMapper;

    @Override
    public List<CustomerResponseDTO> getAllCustomers(String lastName) {
        List<Customer> customers = (lastName != null && !lastName.isBlank())
                ? customerRepository.findByNameLastNameIgnoreCase(lastName)
                : customerRepository.findAll();
        return customers.stream().map(customerMapper::toResponse).toList();
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        return customerMapper.toResponse(findCustomer(id));
    }

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        if (customerRepository.existsByEmailValue(request.getEmail().trim())) {
            throw new DuplicateResourceException(
                    "A customer with email " + request.getEmail() + " already exists");
        }
        if (customerRepository.existsByDriverLicenseNumberValue(request.getDriverLicenseNumber().trim())) {
            throw new DuplicateResourceException(
                    "A customer with driver license " + request.getDriverLicenseNumber() + " already exists");
        }
        return customerMapper.toResponse(customerRepository.save(customerMapper.toEntity(request)));
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {
        Customer customer = findCustomer(id);
        if (customerRepository.existsByEmailValueAndIdNot(request.getEmail().trim(), id)) {
            throw new DuplicateResourceException(
                    "A customer with email " + request.getEmail() + " already exists");
        }
        if (customerRepository.existsByDriverLicenseNumberValueAndIdNot(request.getDriverLicenseNumber().trim(), id)) {
            throw new DuplicateResourceException(
                    "A customer with driver license " + request.getDriverLicenseNumber() + " already exists");
        }
        customerMapper.updateEntity(customer, request);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = findCustomer(id);
        if (rentalRepository.existsByCustomer_Id(id)) {
            throw new ResourceInUseException(
                    "Customer " + id + " has rental history and cannot be deleted");
        }
        customerRepository.delete(customer);
    }

    @Override
    public CustomerWithRentalsResponseDTO getCustomerWithRentals(Long id) {
        Customer customer = findCustomer(id);
        return new CustomerWithRentalsResponseDTO(
                customerMapper.toResponse(customer),
                rentalRepository.findByCustomer_Id(id).stream().map(rentalMapper::toSummary).toList());
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }
}
