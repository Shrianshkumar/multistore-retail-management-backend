package org.pos.retailpossystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
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

    // Create Store
    @PostMapping
    public ResponseEntity<StoreDto> createStore(
            @Valid @RequestBody StoreDto storeDto,
            @RequestHeader("Authorization") String jwt) {

        User user = userService.getUserFromJwtToken(jwt);

        return ResponseEntity.ok(
                storeService.createStore(storeDto, user)
        );
    }

    // Get Store By storeId
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDto> getStoreById(
            @PathVariable Long storeId) {

        return ResponseEntity.ok(
                storeService.getStoreByStoreId(storeId)
        );
    }

    // Update Store
    @PutMapping
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<StoreDto> updateStore(
            @RequestBody StoreDto storeDto) {

        return ResponseEntity.ok(
                storeService.updateStore(storeDto)
        );
    }

    // Delete Store
    @DeleteMapping
    @PreAuthorize("hasAuthority('ROLE_STORE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteStore() {

        storeService.deleteStore();

        return ResponseEntity.ok(
                new ApiResponse("Store deleted successfully")
        );
    }

    // Get Store For Current Admin
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_STORE_ADMIN')")
    public ResponseEntity<StoreDto> getStoreByAdmin() {

        Store store = storeService.getStoreByAdminId();

        return ResponseEntity.ok(
                StoreMapper.mapToDto(store)
        );
    }

    // Get Store For Current Employee
    @GetMapping("/employee")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<StoreDto> getStoreForEmployee() {

        return ResponseEntity.ok(
                storeService.getStoreForCurrentEmployee()
        );
    }

    // Get Employees By Store
    @GetMapping("/{storeId}/employees")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<List<UserDto>> getStoreEmployees(
            @PathVariable Long storeId) {

        return ResponseEntity.ok(
                storeService.getEmployeesByStore(storeId)
        );
    }

    // Add Employee
    @PostMapping("/employees")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<UserDto> addEmployee(
            @RequestBody UserDto userDto) {

        return ResponseEntity.ok(
                storeService.addEmployee(userDto)
        );
    }

    // Super Admin Actions

    // Get All Stores
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<StoreDto>> getAllStores(
            @RequestParam(required = false) StoreStatus status) {

        return ResponseEntity.ok(
                storeService.getAllStores(status)
        );
    }


    // Approve or Decline Store
    @PutMapping("/{storeId}/moderate")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<StoreDto> moderateStore(
            @PathVariable Long storeId,
            @RequestParam StoreStatus action) {

        return ResponseEntity.ok(
                storeService.moderateStore(storeId, action)
        );
    }
}