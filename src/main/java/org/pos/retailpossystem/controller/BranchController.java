package org.pos.retailpossystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.payload.dto.BranchDto;
import org.pos.retailpossystem.service.BranchService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final UserService userService;

    // Create Branch
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<BranchDto> createBranch(
            @Valid @RequestBody BranchDto branchDto,
            @RequestHeader("Authorization") String jwt) {

        User user = userService.getUserFromJwtToken(jwt);

        return ResponseEntity.ok(
                branchService.createBranch(branchDto, user)
        );
    }

    // Get Branch By Id
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchDto> getBranchById(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                branchService.getBranchById(branchId)
        );
    }

    // Get All Branches By Store
    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<List<BranchDto>> getBranchesByStoreId(
            @PathVariable Long storeId) {

        return ResponseEntity.ok(
                branchService.getAllBranchesByStoreId(storeId)
        );
    }

    // Update Branch
    @PutMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<BranchDto> updateBranch(
            @PathVariable Long branchId,
            @RequestBody BranchDto branchDto,
            @RequestHeader("Authorization") String jwt) {

        User user = userService.getUserFromJwtToken(jwt);

        return ResponseEntity.ok(
                branchService.updateBranch(branchId, branchDto, user)
        );
    }

    // Delete Branch
    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<Void> deleteBranch(
            @PathVariable Long branchId) {

        branchService.deleteBranch(branchId);

        return ResponseEntity.noContent().build();
    }
}
