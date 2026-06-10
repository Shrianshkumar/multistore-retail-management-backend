package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.payload.dto.RefundDto;
import org.pos.retailpossystem.service.RefundService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    /**
     * Create refund
     */
    @PostMapping
    public ResponseEntity<RefundDto> createRefund(
            @RequestBody RefundDto refundDto
    ) {

        RefundDto createdRefund = refundService.createRefund(refundDto);

        return ResponseEntity.ok(createdRefund);
    }

    /**
     * Get all refunds
     */
    @GetMapping
    public ResponseEntity<List<RefundDto>> getAllRefunds() {

        return ResponseEntity.ok(
                refundService.getAllRefunds()
        );
    }

    /**
     * Get refunds by cashier
     */
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<RefundDto>> getRefundsByCashier(
            @PathVariable Long cashierId
    ) {

        return ResponseEntity.ok(
                refundService.getRefundsByCashier(cashierId)
        );
    }

    /**
     * Get refunds by branch
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RefundDto>> getRefundsByBranch(
            @PathVariable Long branchId
    ) {

        return ResponseEntity.ok(
                refundService.getRefundsByBranch(branchId)
        );
    }

    /**
     * Get latest 5 refunds by branch
     */
    @GetMapping("/branch/{branchId}/recent")
    public ResponseEntity<List<RefundDto>> getRecentRefundsByBranch(
            @PathVariable Long branchId
    ) {

        return ResponseEntity.ok(
                refundService.getRecentRefundsByBranch(branchId)
        );
    }

    /**
     * Get refunds by shift report
     */
    @GetMapping("/shift/{shiftReportId}")
    public ResponseEntity<List<RefundDto>> getRefundsByShift(
            @PathVariable Long shiftReportId
    ) {

        return ResponseEntity.ok(
                refundService.getRefundsByShiftReport(shiftReportId)
        );
    }

    /**
     * Get refunds by cashier and date range
     */
    @GetMapping("/cashier/{cashierId}/range")
    public ResponseEntity<List<RefundDto>> getRefundsByCashierAndDateRange(

            @PathVariable Long cashierId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {

        return ResponseEntity.ok(
                refundService.getRefundsByCashierAndDateRange(
                        cashierId,
                        from,
                        to
                )
        );
    }

    /**
     * Get refund by ID
     */
    @GetMapping("/{refundId}")
    public ResponseEntity<RefundDto> getRefundById(
            @PathVariable Long refundId
    ) {

        return ResponseEntity.ok(
                refundService.getRefundById(refundId)
        );
    }

    /**
     * Delete refund
     */
    @DeleteMapping("/{refundId}")
    public ResponseEntity<String> deleteRefund(
            @PathVariable Long refundId
    ) {

        refundService.deleteRefund(refundId);

        return ResponseEntity.ok(
                "Refund deleted successfully"
        );
    }
}
