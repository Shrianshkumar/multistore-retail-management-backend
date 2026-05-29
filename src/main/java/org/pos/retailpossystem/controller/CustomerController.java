package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.payload.dto.CustomerDto;
import org.pos.retailpossystem.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Create Customer
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<CustomerDto> createCustomer(
            @RequestBody CustomerDto customerDto) {

        return ResponseEntity.ok(
                customerService.createCustomer(customerDto)
        );
    }

    // Update Customer
    @PutMapping("/{customerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerDto customerDto) {

        return ResponseEntity.ok(
                customerService.updateCustomer(
                        customerId,
                        customerDto
                )
        );
    }

    // Delete Customer
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long customerId) {

        customerService.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }

    // Get Customer By customerId
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                customerService.getCustomerById(customerId)
        );
    }

    // Get All Customers
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {

        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }
}
