package org.pos.retailpossystem.service;

import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.UserException;

import java.util.List;
import java.util.Set;

public interface UserService {
    User getUserByEmail(String email) throws UserException;
    User getUserById(Long id) throws UserException;
    User getUserFromJwtToken(String token) throws UserException;
    User getCurrentUser();
    Set<User> getUserByRole(UserRole role) throws UserException;
    List<User> getUsers() throws UserException;
}
