package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.payload.dto.admin.DashboardOverviewDto;
import org.pos.retailpossystem.payload.dto.admin.StoreRegistrationStatsDto;
import org.pos.retailpossystem.payload.dto.admin.StoreStatusStatsDto;
import org.pos.retailpossystem.repository.StoreRepo;
import org.pos.retailpossystem.repository.projection.StoreRegistrationStatsProjection;
import org.pos.retailpossystem.service.SuperAdminDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuperAdminDashboardServiceImpl implements SuperAdminDashboardService {

    private final StoreRepo storeRepo;

    @Override
    public DashboardOverviewDto getDashboardOverview() {

        Long totalStores = storeRepo.count();

        Long activeStores =
                storeRepo.countByStatus(StoreStatus.ACTIVE);

        Long pendingStores =
                storeRepo.countByStatus(StoreStatus.PENDING);

        Long blockedStores =
                storeRepo.countByStatus(StoreStatus.BLOCKED);

        return DashboardOverviewDto.builder()
                .totalStores(totalStores)
                .activeStores(activeStores)
                .pendingStores(pendingStores)
                .blockedStores(blockedStores)
                .build();
    }

    @Override
    public List<StoreRegistrationStatsDto>
    getLast7DayRegistrationStats() {

        LocalDateTime today = LocalDateTime.now();

        LocalDateTime sevenDaysAgo =
                today.minusDays(6);

        List<StoreRegistrationStatsProjection> rawStats =
                storeRepo.getStoreRegistrationStats(
                        sevenDaysAgo
                );

        Map<String, Long> statsMap =
                new LinkedHashMap<>();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd"
                );

        // Initialize last 7 days with 0 count
        for (int i = 0; i < 7; i++) {

            LocalDate date =
                    sevenDaysAgo.toLocalDate()
                            .plusDays(i);

            statsMap.put(
                    date.format(formatter),
                    0L
            );
        }

        // Replace actual counts
        for (StoreRegistrationStatsProjection stat
                : rawStats) {

            String formattedDate =
                    stat.getDate()
                            .format(formatter);

            statsMap.put(
                    formattedDate,
                    stat.getCount()
            );
        }

        return statsMap.entrySet()
                .stream()
                .map(entry ->
                        StoreRegistrationStatsDto
                                .builder()
                                .date(entry.getKey())
                                .count(entry.getValue())
                                .build()
                )
                .toList();
    }

    @Override
    public StoreStatusStatsDto
    getStoreStatusStats() {

        Long active =
                storeRepo.countByStatus(
                        StoreStatus.ACTIVE
                );

        Long blocked =
                storeRepo.countByStatus(
                        StoreStatus.BLOCKED
                );

        Long pending =
                storeRepo.countByStatus(
                        StoreStatus.PENDING
                );

        return StoreStatusStatsDto.builder()
                .active(active)
                .blocked(blocked)
                .pending(pending)
                .build();
    }

    @Override
    public Long getStoreRegistrationCountBetween(
            LocalDateTime start,
            LocalDateTime end
    ) {

        return storeRepo.countByCreatedAtBetween(
                start,
                end
        );
    }
}

