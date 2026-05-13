package org.pos.retailpossystem.service;

import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.exception.UserException;
import org.pos.retailpossystem.payload.dto.StoreDto;
import org.pos.retailpossystem.payload.dto.UserDto;

import java.util.List;

public interface StoreService {
    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreById(Long id) throws ResourceNotFoundException;
    List<StoreDto> getAllStores(StoreStatus status);
    Store getStoreByAdminId() throws UserException;
    StoreDto getStoreByEmployee() throws UserException;
    StoreDto updateStore(Long id, StoreDto storeDto) throws ResourceNotFoundException, UserException;
    void deleteStore() throws ResourceNotFoundException, UserException;
    UserDto addEmployee(Long id, UserDto userDto) throws UserException;
    List<UserDto> getEmployeesByStore(Long storeId) throws UserException;

    StoreDto moderateStore(Long storeId, StoreStatus action) throws ResourceNotFoundException;

}
