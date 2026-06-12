package org.pos.retailpossystem.payload.dto.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRegistrationStatsDto {
    private String date;
    private Long count;
}
