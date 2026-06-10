package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundRepo extends JpaRepository<Refund, Long> {

    List<Refund> findByCashierId(Long cashierId);

    List<Refund> findByShiftReportId(Long shiftReportId);

    List<Refund> findByCashierIdAndCreatedAtBetween(Long cashierId,
                                                    LocalDateTime start,
                                                    LocalDateTime end);

    List<Refund> findByBranchId(Long branchId);
    List<Refund> findTop5ByBranchIdOrderByCreatedAtDesc(Long branchId);
}
