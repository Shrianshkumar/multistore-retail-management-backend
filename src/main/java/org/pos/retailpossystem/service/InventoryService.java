package org.pos.retailpossystem.service;

import org.pos.retailpossystem.payload.dto.InventoryDto;

import java.util.List;

public interface InventoryService {
    InventoryDto createInventory(InventoryDto inventoryDto);
    InventoryDto updateInventory(
            Long inventoryId,
            InventoryDto inventoryDto
    );
    void deleteInventory(Long inventoryId);
    InventoryDto getInventoryById(Long inventoryId);
    InventoryDto getInventoryByProductId(Long productId);
    List<InventoryDto> getInventoryByBranch(Long branchId);
}
