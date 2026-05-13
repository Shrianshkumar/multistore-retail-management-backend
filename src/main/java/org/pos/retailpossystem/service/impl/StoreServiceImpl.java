package org.pos.retailpossystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.Branch;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.StoreContactInfo;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.exception.UserException;
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
    public StoreDto createStore(StoreDto StoreDto, User user) {

        System.out.println(StoreDto);

        Store store = StoreMapper.toEntity(StoreDto, user);


        return StoreMapper.toDto(storeRepo.save(store));
    }

    @Override
    public StoreDto getStoreById(Long id) throws ResourceNotFoundException {
        Store store = storeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        return StoreMapper.toDto(store);
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
                .map(StoreMapper::toDto)
                .collect(Collectors.toList());


    }

    @Override
    public Store getStoreByAdminId() throws UserException {
        User currentUser=userService.getCurrentUser();
        return storeRepo.findByStoreAdminId(
                currentUser.getId()
        );
    }

    @Override
    public StoreDto getStoreByEmployee() throws UserException {
        User currentUser=userService.getCurrentUser();


        if(currentUser.getStore()==null){
            throw new UserException("user does not have enough permissions to access this store");
        }
        return StoreMapper.toDto(currentUser.getStore());
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto StoreDto) throws ResourceNotFoundException, UserException {
        User currentUser=userService.getCurrentUser();
        Store existing = storeRepo.findByStoreAdminId(currentUser.getId());

        if(existing == null) {
            throw new ResourceNotFoundException("store not found");
        }

        existing.setBrand(StoreDto.getBrand());
        existing.setDescription(StoreDto.getDescription());

        // Convert string storeType to enum, if not null
        if (StoreDto.getStoreType() != null) {
            existing.setStoreType(StoreDto.getStoreType());
        }

        // Set contact info if provided
        if (StoreDto.getContact() != null) {
            StoreContactInfo contact = StoreContactInfo.builder()
                    .address(StoreDto.getContact().getAddress())
                    .phone(StoreDto.getContact().getPhone())
                    .email(StoreDto.getContact().getEmail())
                    .build();
            existing.setContact(contact);
        }

        return StoreMapper.toDto(storeRepo.save(existing));
    }

    @Override
    public void deleteStore() throws ResourceNotFoundException, UserException {
        Store store= getStoreByAdminId();

        if (store==null) {
            throw new ResourceNotFoundException("Store not found");
        }
        storeRepo.deleteById(store.getId());
    }

    @Override
    public UserDto addEmployee(Long id, UserDto UserDto) throws UserException {
        Store store=getStoreByAdminId();

        User employee = UserMapper.toEntity(UserDto);
        if(UserDto.getRole()== UserRole.ROLE_STORE_MANAGER){
            employee.setStore(store);
        }else if(UserDto.getRole()== UserRole.ROLE_BRANCH_MANAGER){
            Branch branch=branchRepo.findById(UserDto.getBranchId()).orElseThrow(
                    ()-> new EntityNotFoundException("branch not found")
            );
            employee.setBranch(branch);
            employee.setStore(store);
        }

        employee.setPassword(passwordEncoder.encode(UserDto.getPassword()));
        User addedEmployee=userRepository.save(employee);

        return UserMapper.toDTO(addedEmployee);
    }

    @Override
    public List<UserDto> getEmployeesByStore(Long storeId) throws UserException {
        User currentUser=userService.getCurrentUser();

        Store store=storeRepo.findById(storeId).orElseThrow(
                ()->new EntityNotFoundException("store not found")
        );
        if(store.getStoreAdmin().getId().equals(currentUser.getId())
                || currentUser.getStore().getId().equals(store.getId())){
            List<User> employees=userRepository.findByStoreId(storeId);
            return UserMapper.toDTOList(employees);
        }

        throw new UserException("user does not have enough permissions to access this store");
    }


    @Override
    public StoreDto moderateStore(Long storeId, StoreStatus action) throws ResourceNotFoundException {
        Store store = storeRepo.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));

        store.setStatus(action);
        Store updatedStore = storeRepo.save(store);
        return StoreMapper.toDto(updatedStore);
    }
}
