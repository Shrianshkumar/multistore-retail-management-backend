package org.pos.retailpossystem.payload.dto.admin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreStatusStatsDto {
    private Long active;
    private Long blocked;
    private Long pending;
}
