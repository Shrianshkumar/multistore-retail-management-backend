package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.payload.dto.UserDto;
import org.pos.retailpossystem.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Create Store Employee
    @PostMapping("/store/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<UserDto> createStoreEmployee(
            @RequestBody UserDto employeeDto,
            @PathVariable Long storeId) {

        UserDto createdEmployee =
                employeeService.createStoreEmployee(
                        employeeDto,
                        storeId
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdEmployee);
    }

    // Create Branch Employee
    @PostMapping("/branch/{branchId}")
    @PreAuthorize("hasAnyAuthority('ROLE_BRANCH_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<UserDto> createBranchEmployee(
            @RequestBody UserDto employeeDto,
            @PathVariable Long branchId) {

        UserDto createdEmployee =
                employeeService.createBranchEmployee(
                        employeeDto,
                        branchId
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdEmployee);
    }

    // Update Employee
    @PutMapping("/{employeeId}")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_STORE_ADMIN',
                'ROLE_STORE_MANAGER',
                'ROLE_BRANCH_MANAGER'
            )
            """)
    public ResponseEntity<UserDto> updateEmployee(
            @PathVariable Long employeeId,
            @RequestBody UserDto employeeDto) {

        UserDto updatedEmployee =
                employeeService.updateEmployee(
                        employeeId,
                        employeeDto
                );

        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete Employee
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long employeeId) {

        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    // Get Employee By Id
    @GetMapping("/{employeeId}")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_STORE_ADMIN',
                'ROLE_STORE_MANAGER',
                'ROLE_BRANCH_MANAGER'
            )
            """)
    public ResponseEntity<UserDto> getEmployeeById(
            @PathVariable Long employeeId) {

        UserDto employee =
                employeeService.getEmployeeById(employeeId);

        return ResponseEntity.ok(employee);
    }

    // Get Store Employees
    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<List<UserDto>> getStoreEmployees(
            @PathVariable Long storeId,
            @RequestParam(required = false) UserRole role) {

        List<UserDto> employees =
                employeeService.getStoreEmployees(
                        storeId,
                        role
                );

        return ResponseEntity.ok(employees);
    }

    // Get Branch Employees
    @GetMapping("/branch/{branchId}")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_STORE_ADMIN',
                'ROLE_BRANCH_MANAGER'
            )
            """)
    public ResponseEntity<List<UserDto>> getBranchEmployees(
            @PathVariable Long branchId,
            @RequestParam(required = false) UserRole role) {

        List<UserDto> employees =
                employeeService.getBranchEmployees(
                        branchId,
                        role
                );

        return ResponseEntity.ok(employees);
    }
}