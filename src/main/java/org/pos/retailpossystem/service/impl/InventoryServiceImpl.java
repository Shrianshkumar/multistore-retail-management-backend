package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.*;
import org.pos.retailpossystem.exception.AccessDeniedException;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.InventoryMapper;
import org.pos.retailpossystem.payload.dto.InventoryDto;
import org.pos.retailpossystem.repository.BranchRepo;
import org.pos.retailpossystem.repository.InventoryRepo;
import org.pos.retailpossystem.repository.ProductRepo;
import org.pos.retailpossystem.service.InventoryService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepo inventoryRepo;
    private final BranchRepo branchRepo;
    private final ProductRepo productRepo;
    private final UserService userService;

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) {

        User currentUser = userService.getCurrentUser();

        Branch branch = branchRepo.findById(inventoryDto.getBranchId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found"));

        Product product = productRepo.findById(inventoryDto.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        validateAuthority(branch.getStore(), currentUser);

        Inventory inventory = InventoryMapper.mapToEntity(
                inventoryDto,
                branch,
                product
        );

        return InventoryMapper.mapToDto(
                inventoryRepo.save(inventory)
        );
    }

    @Override
    public InventoryDto updateInventory(
            Long inventoryId,
            InventoryDto inventoryDto) {

        User currentUser = userService.getCurrentUser();

        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        validateAuthority(inventory.getBranch().getStore(), currentUser);

        inventory.setQuantity(inventoryDto.getQuantity());

        return InventoryMapper.mapToDto(
                inventoryRepo.save(inventory)
        );
    }

    @Override
    public void deleteInventory(Long inventoryId) {

        User currentUser = userService.getCurrentUser();

        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        validateAuthority(inventory.getBranch().getStore(), currentUser);

        inventoryRepo.delete(inventory);
    }

    @Override
    public InventoryDto getInventoryById(Long inventoryId) {

        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        return InventoryMapper.mapToDto(inventory);
    }

    @Override
    public InventoryDto getInventoryByProductId(Long productId) {

        Inventory inventory =
                inventoryRepo.findByProductId(productId);

        if (inventory == null) {
            throw new ResourceNotFoundException("Inventory not found");
        }

        return InventoryMapper.mapToDto(inventory);
    }

    @Override
    public List<InventoryDto> getInventoryByBranch(Long branchId) {

        return inventoryRepo.findByBranchId(branchId)
                .stream()
                .map(InventoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private void validateAuthority(Store store, User user) {

        boolean isStoreManager =
                user.getRole() == UserRole.ROLE_STORE_MANAGER
                        && user.getStore() != null
                        && user.getStore().getId().equals(store.getId());

        boolean isStoreAdmin =
                user.getRole() == UserRole.ROLE_STORE_ADMIN
                        && store.getStoreAdmin() != null
                        && store.getStoreAdmin().getId().equals(user.getId());

        if (isStoreManager || isStoreAdmin) {
            return;
        }

        throw new AccessDeniedException(
                "You are not authorized to manage this store."
        );
    }
}
