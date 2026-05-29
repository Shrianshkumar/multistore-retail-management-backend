package org.pos.retailpossystem.service;

import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.payload.dto.UserDto;

import java.util.List;

public interface EmployeeService {

    UserDto createStoreEmployee(
            UserDto employeeDto,
            Long storeId
    );

    UserDto createBranchEmployee(
            UserDto employeeDto,
            Long branchId
    );

    UserDto updateEmployee(
            Long employeeId,
            UserDto employeeDto
    );

    void deleteEmployee(Long employeeId);

    UserDto getEmployeeById(Long employeeId);

    List<UserDto> getStoreEmployees(
            Long storeId,
            UserRole role
    );

    List<UserDto> getBranchEmployees(
            Long branchId,
            UserRole role
    );
}
