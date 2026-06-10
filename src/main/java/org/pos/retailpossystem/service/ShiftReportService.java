package org.pos.retailpossystem.service;

import org.pos.retailpossystem.payload.dto.ShiftReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftReportService {

    /**
     * Start a new shift for a cashier.
     */
    ShiftReportDto startShift(
            Long cashierId,
            Long branchId
    );

    /**
     * End shift and generate complete shift report.
     */
    ShiftReportDto endShift(
            Long shiftReportId
    );

    /**
     * Get shift report by ID.
     */
    ShiftReportDto getShiftReportById(Long shiftReportId);

    /**
     * Get all shift reports.
     */
    List<ShiftReportDto> getAllShiftReports();

    /**
     * Get all shift reports of a cashier.
     */
    List<ShiftReportDto> getShiftReportsByCashier(
            Long cashierId
    );

    /**
     * Get current active shift progress.
     */
    ShiftReportDto getCurrentShiftProgress(
            Long cashierId
    );

    /**
     * Get all shift reports of a branch.
     */
    List<ShiftReportDto> getShiftReportsByBranch(
            Long branchId
    );

    /**
     * Get cashier shift report for a specific date.
     */
    ShiftReportDto getShiftReportByCashierAndDate(
            Long cashierId,
            LocalDateTime date
    );

    /**
     * Delete shift report by ID.
     */
    void deleteShiftReport(Long shiftReportId);
}
