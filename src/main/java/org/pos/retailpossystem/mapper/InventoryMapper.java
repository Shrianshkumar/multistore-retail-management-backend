package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.Branch;
import org.pos.retailpossystem.entity.Inventory;
import org.pos.retailpossystem.entity.Product;
import org.pos.retailpossystem.payload.dto.InventoryDto;

public class InventoryMapper {
    public static InventoryDto mapToDto(Inventory inventory) {
        return InventoryDto.builder()
                .id(inventory.getId())
                .branchId(inventory.getBranch().getId())
                .productId(inventory.getProduct().getId())
                .quantity(inventory.getQuantity())
                .build();
    }

    public static Inventory mapToEntity(InventoryDto dto, Branch branch, Product product) {
        return Inventory.builder()
                .id(dto.getId())
                .branch(branch)
                .product(product)
                .quantity(dto.getQuantity())
                .build();
    }
}
