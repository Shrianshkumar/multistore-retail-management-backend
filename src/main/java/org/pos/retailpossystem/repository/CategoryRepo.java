package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> getByStoreId(Long storeId);
}
