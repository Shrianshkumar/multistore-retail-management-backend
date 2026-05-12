package org.pos.retailpossystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.configuration.JwtProvider;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.UserException;
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
    /**
     * @param email
     * @return user
     */
    @Override
    public User getUserByEmail(String email) throws UserException {
        User user=userRepo.findByEmail(email);
        if(user==null){
            throw new UserException("User not found with email: "+email);
        }
        return user;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public User getUserById(Long id) throws UserException {
        return userRepo.findById(id).orElse(null);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public User getUserFromJwtToken(String token) throws UserException {
        String email = jwtProvider.getEmailFromJwtToken(token);
        User user = userRepo.findByEmail(email);
        if(user==null) throw new UserException("user not exist with email "+email);
        return user;
    }

    /**
     * @return user
     */
    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user= userRepo.findByEmail(email);
        if(user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    /**
     * @param role
     * @return
     * @throws UserException
     */
    @Override
    public Set<User> getUserByRole(UserRole role) throws UserException {
        return userRepo.findByRole(role);
    }

    /**
     * @return
     * @throws UserException
     */
    @Override
    public List<User> getUsers() throws UserException {
        return userRepo.findAll();
    }
}
