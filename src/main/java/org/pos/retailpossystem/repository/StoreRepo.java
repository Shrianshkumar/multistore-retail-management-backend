package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.repository.projection.StoreRegistrationStatsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreRepo extends JpaRepository<Store, Long> {
    Store findByStoreAdminId(Long storeAdminId);

    List<Store> findByStatus(StoreStatus storeStatus);

    Long countByStatus(StoreStatus status);

    Long countByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
    SELECT
        DATE(s.createdAt) AS date,
        COUNT(s) AS count
    FROM Store s
    WHERE s.createdAt >= :startDate
    GROUP BY DATE(s.createdAt)
    ORDER BY DATE(s.createdAt) ASC
""")
    List<StoreRegistrationStatsProjection>
    getStoreRegistrationStats(
            @Param("startDate")
            LocalDateTime startDate
    );
}
