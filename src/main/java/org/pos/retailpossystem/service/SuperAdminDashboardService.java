package org.pos.retailpossystem.service;

import org.pos.retailpossystem.payload.dto.admin.DashboardOverviewDto;
import org.pos.retailpossystem.payload.dto.admin.StoreRegistrationStatsDto;
import org.pos.retailpossystem.payload.dto.admin.StoreStatusStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SuperAdminDashboardService {
    DashboardOverviewDto getDashboardOverview();
    List<StoreRegistrationStatsDto> getLast7DayRegistrationStats();
    StoreStatusStatsDto getStoreStatusStats();
    Long getStoreRegistrationCountBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}
