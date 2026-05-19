package org.pos.retailpossystem.service;

import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.UserException;

import java.util.List;
import java.util.Set;

public interface UserService {
    User getUserByEmail(String email);
    User getUserById(Long userId);
    User getUserFromJwtToken(String token);
    User getCurrentUser();
    Set<User> getUsersByRole(UserRole role);
    List<User> getUsers();
}
