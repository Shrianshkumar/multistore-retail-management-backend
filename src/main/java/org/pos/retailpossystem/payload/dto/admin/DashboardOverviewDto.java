package org.pos.retailpossystem.payload.dto.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardOverviewDto {
    private Long totalStores;
    private Long activeStores;
    private Long blockedStores;
    private Long pendingStores;
}
