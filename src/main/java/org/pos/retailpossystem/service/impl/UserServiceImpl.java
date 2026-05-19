package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.configuration.JwtProvider;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.repository.UserRepo;
import org.pos.retailpossystem.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final JwtProvider jwtProvider;

    @Override
    public User getUserByEmail(String email) {

        return userRepo.findByEmail(email);
    }

    @Override
    public User getUserById(Long userId) {

        return userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getUserFromJwtToken(String token) {

        String email = jwtProvider.getEmailFromJwtToken(token);

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found with email: " + email
            );
        }

        return user;
    }

    @Override
    public User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return user;
    }

    @Override
    public Set<User> getUsersByRole(UserRole role) {

        return userRepo.findByRole(role);
    }

    @Override
    public List<User> getUsers() {

        return userRepo.findAll();
    }
}
