package org.pos.retailpossystem.service;

import org.pos.retailpossystem.payload.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto createCustomer(
            CustomerDto customerDto
    );

    CustomerDto updateCustomer(
            Long customerId,
            CustomerDto customerDto
    );

    void deleteCustomer(Long customerId);

    CustomerDto getCustomerById(
            Long customerId
    );

    List<CustomerDto> getAllCustomers();
}
