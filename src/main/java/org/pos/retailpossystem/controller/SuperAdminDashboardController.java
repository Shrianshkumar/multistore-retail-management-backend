package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.payload.dto.admin.DashboardOverviewDto;
import org.pos.retailpossystem.payload.dto.admin.StoreRegistrationStatsDto;
import org.pos.retailpossystem.payload.dto.admin.StoreStatusStatsDto;
import org.pos.retailpossystem.service.SuperAdminDashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/super-admin/dashboard")
@RequiredArgsConstructor
public class SuperAdminDashboardController {

    private final SuperAdminDashboardService superAdminDashboardService;

    /**
     * Get dashboard overview statistics
     */
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDto> getDashboardOverview() {

        return ResponseEntity.ok(
                superAdminDashboardService
                        .getDashboardOverview()
        );
    }

    /**
     * Get last 7 days registration statistics
     */
    @GetMapping("/store-registrations")
    public ResponseEntity<List<StoreRegistrationStatsDto>> getLast7DayRegistrationStats() {

        return ResponseEntity.ok(
                superAdminDashboardService
                        .getLast7DayRegistrationStats()
        );
    }

    /**
     * Get store status statistics
     */
    @GetMapping("/store-status-stats")
    public ResponseEntity<StoreStatusStatsDto> getStoreStatusStats() {

        return ResponseEntity.ok(
                superAdminDashboardService
                        .getStoreStatusStats()
        );
    }

    @GetMapping("/store-registrations/count")
    public ResponseEntity<Long>
    getStoreRegistrationCountBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start, @RequestParam @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(
                superAdminDashboardService
                        .getStoreRegistrationCountBetween(
                                start,
                                end
                        )
        );
    }
}
