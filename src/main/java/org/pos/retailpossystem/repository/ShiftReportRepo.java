package org.pos.retailpossystem.repository;

import org.pos.retailpossystem.entity.ShiftReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftReportRepo extends JpaRepository<ShiftReport, Long> {

    List<ShiftReport> findByCashierId(Long cashierId);

    List<ShiftReport> findByBranchId(Long branchId);

    /**
     * Get latest open shift for a cashier
     * where shiftEnd is null.
     */
    Optional<ShiftReport>
    findTopByCashierIdAndShiftEndIsNullOrderByShiftStartDesc(
            Long cashierId
    );

    /**
     * Get shift report for a specific date
     * for a cashier.
     */
    Optional<ShiftReport>
    findByCashierIdAndShiftStartBetween(
            Long cashierId,
            LocalDateTime start,
            LocalDateTime end
    );
}
