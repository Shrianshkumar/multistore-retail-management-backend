package org.pos.retailpossystem.service;

import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.payload.dto.StoreDto;
import org.pos.retailpossystem.payload.dto.UserDto;

import java.util.List;

public interface StoreService {
    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreByStoreId(Long storeId);
    List<StoreDto> getAllStores(StoreStatus status);
    Store getStoreByAdminId();
    StoreDto getStoreForCurrentEmployee();
    StoreDto updateStore(StoreDto storeDto);
    void deleteStore();
    UserDto addEmployee(UserDto userDto);
    List<UserDto> getEmployeesByStore(Long storeId);
    StoreDto moderateStore(Long storeId, StoreStatus action);

}
