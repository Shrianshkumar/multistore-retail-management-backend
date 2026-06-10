package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.payload.dto.ShiftReportDto;
import org.pos.retailpossystem.service.ShiftReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shift-reports")
@RequiredArgsConstructor
public class ShiftReportController {

    private final ShiftReportService shiftReportService;

    /**
     * Start a new shift
     */
    @PostMapping("/start")
    public ResponseEntity<ShiftReportDto> startShift(
            @RequestParam Long branchId
    ) {

        ShiftReportDto shiftReport =
                shiftReportService.startShift(
                        null,
                        branchId
                );

        return ResponseEntity.ok(shiftReport);
    }

    /**
     * End current active shift
     */
    @PatchMapping("/end")
    public ResponseEntity<ShiftReportDto> endShift() {

        ShiftReportDto shiftReport =
                shiftReportService.endShift(null);

        return ResponseEntity.ok(shiftReport);
    }

    /**
     * Get current shift progress
     */
    @GetMapping("/current")
    public ResponseEntity<ShiftReportDto>
    getCurrentShiftProgress() {

        ShiftReportDto shiftReport =
                shiftReportService
                        .getCurrentShiftProgress(null);

        return ResponseEntity.ok(shiftReport);
    }

    /**
     * Get shift report by date
     */
    @GetMapping("/cashier/{cashierId}/by-date")
    public ResponseEntity<ShiftReportDto>
    getShiftReportByDate(

            @PathVariable Long cashierId,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime date
    ) {

        ShiftReportDto shiftReport =
                shiftReportService
                        .getShiftReportByCashierAndDate(
                                cashierId,
                                date
                        );

        return ResponseEntity.ok(shiftReport);
    }

    /**
     * Get all shifts of a cashier
     */
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<ShiftReportDto>>
    getShiftsByCashier(
            @PathVariable Long cashierId
    ) {

        return ResponseEntity.ok(
                shiftReportService
                        .getShiftReportsByCashier(
                                cashierId
                        )
        );
    }

    /**
     * Get all shifts of a branch
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<ShiftReportDto>>
    getShiftsByBranch(
            @PathVariable Long branchId
    ) {

        return ResponseEntity.ok(
                shiftReportService
                        .getShiftReportsByBranch(
                                branchId
                        )
        );
    }

    /**
     * Get all shift reports
     */
    @GetMapping
    public ResponseEntity<List<ShiftReportDto>>
    getAllShifts() {

        return ResponseEntity.ok(
                shiftReportService
                        .getAllShiftReports()
        );
    }

    /**
     * Get shift report by ID
     */
    @GetMapping("/{shiftReportId}")
    public ResponseEntity<ShiftReportDto>
    getShiftById(
            @PathVariable Long shiftReportId
    ) {

        return ResponseEntity.ok(
                shiftReportService
                        .getShiftReportById(
                                shiftReportId
                        )
        );
    }

    /**
     * Delete shift report
     */
    @DeleteMapping("/{shiftReportId}")
    public ResponseEntity<String>
    deleteShift(
            @PathVariable Long shiftReportId
    ) {

        shiftReportService.deleteShiftReport(
                shiftReportId
        );

        return ResponseEntity.ok(
                "Shift report deleted successfully"
        );
    }
}
