package org.pos.retailpossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pos.retailpossystem.domain.enums.StoreStatus;
import org.pos.retailpossystem.entity.StoreContactInfo;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {
    private Long id;
    private String brand;
    private Long storeAdminId;
    private UserDto storeAdmin;
    private String storeType;
    private StoreStatus status;
    private String description;
    private StoreContactInfo contact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
