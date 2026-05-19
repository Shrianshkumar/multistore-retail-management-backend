package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.mapper.UserMapper;
import org.pos.retailpossystem.payload.dto.UserDto;
import org.pos.retailpossystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get Current User Profile
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile() {

        User user = userService.getCurrentUser();

        UserDto userDto = UserMapper.mapToDto(user);

        return ResponseEntity.ok(userDto);
    }

    // Get Customers
    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Set<UserDto>> getCustomerList() {

        Set<User> users =
                userService.getUsersByRole(UserRole.ROLE_CUSTOMER);

        Set<UserDto> userDtoSet =
                UserMapper.mapToDtoSet(users);

        return ResponseEntity.ok(userDtoSet);
    }

    // Get Cashiers
    @GetMapping("/cashiers")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<Set<UserDto>> getCashierList() {

        Set<User> users =
                userService.getUsersByRole(UserRole.ROLE_BRANCH_CASHIER);

        Set<UserDto> userDtoSet =
                UserMapper.mapToDtoSet(users);

        return ResponseEntity.ok(userDtoSet);
    }

    // Get All Users
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<UserDto>> getUsersList() {

        List<UserDto> users = userService.getUsers()
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // Get User By userId
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Long userId) {

        User user = userService.getUserById(userId);

        UserDto userDto = UserMapper.mapToDto(user);

        return ResponseEntity.ok(userDto);
    }
}