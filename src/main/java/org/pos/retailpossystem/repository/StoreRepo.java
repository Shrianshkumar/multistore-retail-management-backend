package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepo extends JpaRepository<Store, Long> {
    Store findByStoreAdminId(Long storeAdminId);

    List<Store> findByStatus(StoreStatus storeStatus);
}
