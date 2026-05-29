package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.payload.dto.InventoryDto;
import org.pos.retailpossystem.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Create Inventory
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<InventoryDto> createInventory(
            @RequestBody InventoryDto inventoryDto) {

        return ResponseEntity.ok(
                inventoryService.createInventory(inventoryDto)
        );
    }

    // Update Inventory
    @PutMapping("/{inventoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<InventoryDto> updateInventory(
            @PathVariable Long inventoryId,
            @RequestBody InventoryDto inventoryDto) {

        return ResponseEntity.ok(
                inventoryService.updateInventory(
                        inventoryId,
                        inventoryDto
                )
        );
    }

    // Delete Inventory
    @DeleteMapping("/{inventoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_STORE_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable Long inventoryId) {

        inventoryService.deleteInventory(inventoryId);

        return ResponseEntity.noContent().build();
    }

    // Get Inventory By Id
    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryDto> getInventoryById(
            @PathVariable Long inventoryId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(inventoryId)
        );
    }

    // Get Inventory By Product Id
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryDto> getInventoryByProductId(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByProductId(productId)
        );
    }

    // Get Inventory By Branch
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryDto>> getInventoryByBranch(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByBranch(branchId)
        );
    }
}
