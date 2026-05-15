package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByStoreId(Long storeId);
    @Query("SELECT p FROM Product p " +
            "WHERE p.store.id = :storeId AND (" +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%'))" +
            ")"
    )
    List<Product> searchProducts(@Param("storeId") Long storeId,
                                  @Param("query") String query);
}
