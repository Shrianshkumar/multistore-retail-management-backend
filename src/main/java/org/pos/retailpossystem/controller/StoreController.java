package org.pos.retailpossystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.exception.UserException;
import org.pos.retailpossystem.mapper.StoreMapper;
import org.pos.retailpossystem.payload.dto.StoreDto;
import org.pos.retailpossystem.payload.dto.UserDto;
import org.pos.retailpossystem.payload.response.ApiResponse;
import org.pos.retailpossystem.service.StoreService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;

    // 🔹 Create Store
    @PostMapping
    public ResponseEntity<StoreDto> createStore(@Valid @RequestBody StoreDto StoreDto,
                                                @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(storeService.createStore(StoreDto, user));
    }

    // 🔹 Get Store by ID
    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }



    // 🔹 Update Store
    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> updateStore(
            @PathVariable Long id,
            @RequestBody StoreDto StoreDto)
            throws ResourceNotFoundException,
            UserException {
        return ResponseEntity.ok(storeService.updateStore(id, StoreDto));
    }

    // 🔹 Delete Store
    @DeleteMapping()
    public ResponseEntity<ApiResponse> deleteStore()
            throws ResourceNotFoundException, UserException {
        storeService.deleteStore();
        return ResponseEntity.ok(new ApiResponse("store deleted successfully"));
    }



    // ✅ Get Stores by Admin User ID
    @GetMapping("/admin")
    public ResponseEntity<StoreDto> getStoresByAdminId() throws UserException {
        Store store=storeService.getStoreByAdminId();
        return ResponseEntity.ok(StoreMapper.toDto(store));
    }

    @GetMapping("/employee")
    public ResponseEntity<StoreDto> getStoresByEmployee() throws UserException {
        StoreDto store=storeService.getStoreByEmployee();
        return ResponseEntity.ok(store);
    }

    @GetMapping("/{storeId}/employee/list")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<List<UserDto>> getStoreEmployeeList(
            @PathVariable Long storeId) throws UserException {
        List<UserDto> users=storeService.getEmployeesByStore(storeId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add/employee")
    @PreAuthorize("hasAnyAuthority('STORE_MANAGER','STORE_ADMIN')")
    public ResponseEntity<UserDto> addEmployee(
            @RequestBody UserDto UserDto) throws UserException {
        UserDto user=storeService.addEmployee(null, UserDto);
        return ResponseEntity.ok(user);
    }
}
