package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

}
