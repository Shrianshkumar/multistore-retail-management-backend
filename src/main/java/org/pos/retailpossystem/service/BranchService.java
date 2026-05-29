package org.pos.retailpossystem.service;

import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.payload.dto.BranchDto;

import java.util.List;

public interface BranchService {

    BranchDto createBranch(BranchDto branchDto, User user);

    BranchDto getBranchById(Long branchId);

    List<BranchDto> getAllBranchesByStoreId(Long storeId);

    BranchDto updateBranch(Long branchId,
                           BranchDto branchDto,
                           User user);

    void deleteBranch(Long branchId);
}
