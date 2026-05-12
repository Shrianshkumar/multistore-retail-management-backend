package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.configuration.JwtProvider;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.UserException;
import org.pos.retailpossystem.mapper.UserMapper;
import org.pos.retailpossystem.payload.dto.UserDto;
import org.pos.retailpossystem.repository.UserRepo;
import org.pos.retailpossystem.service.UserService;
import org.pos.retailpossystem.service.impl.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    @GetMapping("/api/users/profile")
    public ResponseEntity<UserDto> getUserProfileFromJwtHandler(
            @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.getUserFromJwtToken(jwt);
        UserDto UserDto=UserMapper.toDTO(user);

        return new ResponseEntity<>(UserDto, HttpStatus.OK);
    }

    @GetMapping("/api/users/customer")
    public ResponseEntity<Set<UserDto>> getCustomerList(
            @RequestHeader("Authorization") String jwt) throws UserException {
        Set<User> users = userService.getUserByRole(UserRole.ROLE_CUSTOMER);
        Set<UserDto> UserDto=UserMapper.toDTOSet(users);

        return new ResponseEntity<>(UserDto,HttpStatus.OK);
    }

    @GetMapping("/api/users/cashier")
    public ResponseEntity<Set<UserDto>> getCashierList(
            @RequestHeader("Authorization") String jwt) throws UserException {
        Set<User> users = userService.getUserByRole(UserRole.ROLE_BRANCH_CASHIER);
        Set<UserDto> UserDto=UserMapper.toDTOSet(users);

        return new ResponseEntity<>(UserDto,HttpStatus.OK);
    }

    @GetMapping("/users/list")
    public ResponseEntity<List<User>> getUsersListHandler(
            @RequestHeader("Authorization") String jwt) throws UserException {
        List<User> users = userService.getUsers();

        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserByIdHandler(
            @PathVariable Long userId
    ) throws UserException {
        User user = userService.getUserById(userId);
        UserDto UserDto= UserMapper.toDTO(user);

        return new ResponseEntity<>(UserDto,HttpStatus.OK);
    }
}
