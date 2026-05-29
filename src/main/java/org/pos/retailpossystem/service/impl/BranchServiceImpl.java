package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.Branch;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.AccessDeniedException;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.BranchMapper;
import org.pos.retailpossystem.payload.dto.BranchDto;
import org.pos.retailpossystem.repository.BranchRepo;
import org.pos.retailpossystem.repository.StoreRepo;
import org.pos.retailpossystem.service.BranchService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepo branchRepo;
    private final StoreRepo storeRepo;
    private final UserService userService;

    @Override
    public BranchDto createBranch(BranchDto branchDto, User user) {

        Store store = storeRepo.findByStoreAdminId(user.getId());

        if (store == null) {
            throw new ResourceNotFoundException("Store not found");
        }

        validateAuthority(store, user);

        Branch branch = BranchMapper.mapToEntity(branchDto, store);

        return BranchMapper.toDto(
                branchRepo.save(branch)
        );
    }

    @Override
    public BranchDto getBranchById(Long branchId) {

        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found"));

        return BranchMapper.toDto(branch);
    }

    @Override
    public List<BranchDto> getAllBranchesByStoreId(Long storeId) {

        User currentUser = userService.getCurrentUser();

        Store store = storeRepo.findById(storeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Store not found"));

        validateAuthority(store, currentUser);

        return branchRepo.findByStoreId(store.getId())
                .stream()
                .map(BranchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BranchDto updateBranch(
            Long branchId,
            BranchDto branchDto,
            User user) {

        Branch existing = branchRepo.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found"));

        validateAuthority(existing.getStore(), user);

        existing.setName(branchDto.getName());
        existing.setAddress(branchDto.getAddress());
        existing.setEmail(branchDto.getEmail());
        existing.setPhone(branchDto.getPhone());
        existing.setCloseTime(branchDto.getCloseTime());
        existing.setOpenTime(branchDto.getOpenTime());
        existing.setWorkingDays(branchDto.getWorkingDays());

        return BranchMapper.toDto(
                branchRepo.save(existing)
        );
    }

    @Override
    public void deleteBranch(Long branchId) {

        User currentUser = userService.getCurrentUser();

        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found"));

        validateAuthority(branch.getStore(), currentUser);

        branchRepo.delete(branch);
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