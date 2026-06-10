package org.pos.retailpossystem.service;

import org.pos.retailpossystem.payload.dto.RefundDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundService {

    /**
     * Create a refund for an order.
     */
    RefundDto createRefund(RefundDto refundDto);

    /**
     * Get all refunds.
     */
    List<RefundDto> getAllRefunds();

    /**
     * Get all refunds processed by a specific cashier.
     */
    List<RefundDto> getRefundsByCashier(Long cashierId);

    /**
     * Get refunds for a specific shift report.
     */
    List<RefundDto> getRefundsByShiftReport(Long shiftReportId);

    /**
     * Get refunds by cashier within a date range.
     */
    List<RefundDto> getRefundsByCashierAndDateRange(
            Long cashierId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Get all refunds processed in a specific branch.
     */
    List<RefundDto> getRefundsByBranch(Long branchId);

    /**
     * Get latest 5 refunds for a branch.
     */
    List<RefundDto> getRecentRefundsByBranch(Long branchId);

    /**
     * Get refund by ID.
     */
    RefundDto getRefundById(Long refundId);

    /**
     * Delete refund by ID.
     */
    void deleteRefund(Long refundId);
}
