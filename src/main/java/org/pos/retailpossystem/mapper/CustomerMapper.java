package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.Customer;
import org.pos.retailpossystem.payload.dto.CustomerDto;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerDto mapToDto(Customer customer) {

        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    public static Customer mapToEntity(
            CustomerDto customerDto) {

        return Customer.builder()
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .phone(customerDto.getPhone())
                .build();
    }

    public static List<CustomerDto> mapToDtoList(
            List<Customer> customers) {

        return customers.stream()
                .map(CustomerMapper::mapToDto)
                .collect(Collectors.toList());
    }
}