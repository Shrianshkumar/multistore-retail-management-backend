package org.pos.retailpossystem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.Branch;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.BadRequestException;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.UserMapper;
import org.pos.retailpossystem.payload.dto.UserDto;
import org.pos.retailpossystem.repository.BranchRepo;
import org.pos.retailpossystem.repository.StoreRepo;
import org.pos.retailpossystem.repository.UserRepo;
import org.pos.retailpossystem.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepo userRepo;
    private final StoreRepo storeRepo;
    private final BranchRepo branchRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createStoreEmployee(
            UserDto userDto,
            Long storeId) {

        Store store = storeRepo.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Store not found with ID: " + storeId
                        ));

        User existingUser = userRepo.findByEmail(userDto.getEmail());

        if (existingUser != null) {
            throw new BadRequestException(
                    "User already exists with email: "
                            + userDto.getEmail()
            );
        }

        Branch branch = null;

        if (userDto.getRole() == UserRole.ROLE_BRANCH_MANAGER) {

            if (userDto.getBranchId() == null) {
                throw new BadRequestException(
                        "Branch ID is required for branch manager role"
                );
            }

            branch = branchRepo.findById(userDto.getBranchId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Branch not found with ID: "
                                            + userDto.getBranchId()
                            ));
        }

        User employee = UserMapper.toEntity(userDto);

        employee.setStore(store);
        employee.setBranch(branch);

        employee.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        User savedEmployee = userRepo.save(employee);

        if (userDto.getRole() == UserRole.ROLE_BRANCH_MANAGER
                && branch != null) {

            branch.setManager(savedEmployee);

            branchRepo.save(branch);
        }

        return UserMapper.mapToDto(savedEmployee);
    }

    @Override
    public UserDto createBranchEmployee(
            UserDto userDto,
            Long branchId) {

        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Branch not found with ID: " + branchId
                        ));

        if (!(userDto.getRole() == UserRole.ROLE_BRANCH_CASHIER
                || userDto.getRole() == UserRole.ROLE_BRANCH_MANAGER)) {

            throw new BadRequestException(
                    "Invalid role for branch employee"
            );
        }

        User existingUser = userRepo.findByEmail(userDto.getEmail());

        if (existingUser != null) {
            throw new BadRequestException(
                    "User already exists with email: "
                            + userDto.getEmail()
            );
        }

        User employee = UserMapper.toEntity(userDto);

        employee.setBranch(branch);
        employee.setStore(branch.getStore());

        employee.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        User savedEmployee = userRepo.save(employee);

        return UserMapper.mapToDto(savedEmployee);
    }

    @Override
    public UserDto updateEmployee(
            Long employeeId,
            UserDto userDto) {

        User existingEmployee = userRepo.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: "
                                        + employeeId
                        ));

        if (userDto.getFullName() != null) {
            existingEmployee.setFullName(userDto.getFullName());
        }

        if (userDto.getEmail() != null) {
            existingEmployee.setEmail(userDto.getEmail());
        }

        if (userDto.getPhone() != null) {
            existingEmployee.setPhone(userDto.getPhone());
        }

        if (userDto.getRole() != null) {
            existingEmployee.setRole(userDto.getRole());
        }

        User updatedEmployee = userRepo.save(existingEmployee);

        return UserMapper.mapToDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {

        User employee = userRepo.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: "
                                        + employeeId
                        ));

        userRepo.delete(employee);
    }

    @Override
    public UserDto getEmployeeById(Long employeeId) {

        User employee = userRepo.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: "
                                        + employeeId
                        ));

        return UserMapper.mapToDto(employee);
    }

    @Override
    public List<UserDto> getStoreEmployees(
            Long storeId,
            UserRole role) {

        Store store = storeRepo.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Store not found with ID: " + storeId
                        ));

        List<User> employees;

        if (role != null) {

            employees = userRepo.findByStoreAndRoleIn(
                    store,
                    List.of(role)
            );

        } else {

            employees = userRepo.findByStoreAndRoleIn(
                    store,
                    List.of(
                            UserRole.ROLE_STORE_ADMIN,
                            UserRole.ROLE_STORE_MANAGER,
                            UserRole.ROLE_BRANCH_MANAGER,
                            UserRole.ROLE_BRANCH_CASHIER
                    )
            );
        }

        return employees.stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getBranchEmployees(
            Long branchId,
            UserRole role) {

        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Branch not found with ID: " + branchId
                        ));

        List<User> employees = userRepo.findByBranchId(branch.getId())
                .stream()
                .filter(user ->
                        role == null || user.getRole() == role
                )
                .collect(Collectors.toList());

        return employees.stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }
}