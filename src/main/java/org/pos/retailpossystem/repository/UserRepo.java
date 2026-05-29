package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Set<User> findByRole(UserRole role);
    List<User> findByBranchId(Long branchId);
    List<User>findByStoreId(Long storeId);
    // find user by store and role
    List<User> findByStoreAndRoleIn(Store store, List<UserRole> roles);
}
