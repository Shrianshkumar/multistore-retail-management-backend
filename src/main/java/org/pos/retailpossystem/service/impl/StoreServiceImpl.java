package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.Branch;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.StoreContactInfo;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.AccessDeniedException;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.StoreMapper;
import org.pos.retailpossystem.mapper.UserMapper;
import org.pos.retailpossystem.payload.dto.StoreDto;
import org.pos.retailpossystem.payload.dto.UserDto;
import org.pos.retailpossystem.repository.BranchRepo;
import org.pos.retailpossystem.repository.StoreRepo;
import org.pos.retailpossystem.repository.UserRepo;
import org.pos.retailpossystem.service.StoreService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepo storeRepo;
    private final UserService userService;
    private final BranchRepo branchRepo;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {
        Store store = StoreMapper.mapToEntity(storeDto, user);
        return StoreMapper.mapToDto(
                storeRepo.save(store)
        );
    }

    @Override
    public StoreDto getStoreByStoreId(Long storeId) {
        Store store = storeRepo.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Store not found"));

        return StoreMapper.mapToDto(store);
    }

    @Override
    public List<StoreDto> getAllStores(StoreStatus status) {

        List<Store> stores;

        if (status != null) {
            stores = storeRepo.findByStatus(status);
        } else {
            stores = storeRepo.findAll();
        }

        return stores.stream()
                .map(StoreMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Store getStoreByAdminId() {

        User currentUser = userService.getCurrentUser();

        return storeRepo.findByStoreAdminId(currentUser.getId());
    }

    @Override
    public StoreDto getStoreForCurrentEmployee() {

        User currentUser = userService.getCurrentUser();

        if (currentUser.getStore() == null) {
            throw new AccessDeniedException(
                    "User does not have permission to access this store"
            );
        }

        return StoreMapper.mapToDto(currentUser.getStore());
    }

    @Override
    public StoreDto updateStore(StoreDto storeDto) {

        User currentUser = userService.getCurrentUser();

        Store existing = storeRepo.findByStoreAdminId(currentUser.getId());

        if (existing == null) {
            throw new ResourceNotFoundException("Store not found");
        }

        existing.setStoreName(storeDto.getStoreName());
        existing.setDescription(storeDto.getDescription());

        if (storeDto.getStoreType() != null) {
            existing.setStoreType(storeDto.getStoreType());
        }

        if (storeDto.getContact() != null) {

            StoreContactInfo contact = StoreContactInfo.builder()
                    .address(storeDto.getContact().getAddress())
                    .phone(storeDto.getContact().getPhone())
                    .email(storeDto.getContact().getEmail())
                    .build();

            existing.setContact(contact);
        }

        return StoreMapper.mapToDto(
                storeRepo.save(existing)
        );
    }

    @Override
    public void deleteStore() {

        Store store = getStoreByAdminId();

        if (store == null) {
            throw new ResourceNotFoundException("Store not found");
        }

        storeRepo.deleteById(store.getId());
    }

    @Override
    public UserDto addEmployee(UserDto userDto) {

        Store store = getStoreByAdminId();

        if (store == null) {
            throw new ResourceNotFoundException("Store not found");
        }

        User employee = UserMapper.toEntity(userDto);

        if (userDto.getRole() == UserRole.ROLE_STORE_MANAGER) {

            employee.setStore(store);

        } else if (userDto.getRole() == UserRole.ROLE_BRANCH_MANAGER) {

            Branch branch = branchRepo.findById(userDto.getBranchId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Branch not found"));

            employee.setBranch(branch);
            employee.setStore(store);
        }

        employee.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        User addedEmployee = userRepository.save(employee);

        return UserMapper.toDTO(addedEmployee);
    }

    @Override
    public List<UserDto> getEmployeesByStore(Long storeId) {

        User currentUser = userService.getCurrentUser();

        Store store = storeRepo.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Store not found"));

        boolean isStoreAdmin =
                store.getStoreAdmin().getId().equals(currentUser.getId());

        boolean belongsToStore =
                currentUser.getStore() != null
                        && currentUser.getStore().getId().equals(store.getId());

        if (isStoreAdmin || belongsToStore) {

            List<User> employees =
                    userRepository.findByStoreId(storeId);

            return UserMapper.toDTOList(employees);
        }

        throw new AccessDeniedException(
                "User does not have permission to access this store"
        );
    }

    @Override
    public StoreDto moderateStore(Long storeId, StoreStatus action) {

        Store store = storeRepo.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Store not found with id: " + storeId
                        ));

        store.setStatus(action);

        Store updatedStore = storeRepo.save(store);

        return StoreMapper.mapToDto(updatedStore);
    }
}