package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.entity.Customer;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.CustomerMapper;
import org.pos.retailpossystem.payload.dto.CustomerDto;
import org.pos.retailpossystem.repository.CustomerRepo;
import org.pos.retailpossystem.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    @Override
    public CustomerDto createCustomer(
            CustomerDto customerDto) {

        Customer customer =
                CustomerMapper.mapToEntity(customerDto);

        return CustomerMapper.mapToDto(
                customerRepo.save(customer)
        );
    }

    @Override
    public CustomerDto updateCustomer(
            Long customerId,
            CustomerDto customerDto) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id "
                                        + customerId));

        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());

        return CustomerMapper.mapToDto(
                customerRepo.save(customer)
        );
    }

    @Override
    public CustomerDto getCustomerById(
            Long customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id "
                                        + customerId));

        return CustomerMapper.mapToDto(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id "
                                        + customerId));

        customerRepo.delete(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {

        return CustomerMapper.mapToDtoList(
                customerRepo.findAll()
        );
    }
}
