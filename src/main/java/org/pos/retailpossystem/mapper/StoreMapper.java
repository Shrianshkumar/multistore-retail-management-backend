package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.payload.dto.StoreDto;
import org.springframework.stereotype.Service;

@Service
public class StoreMapper {
    public static StoreDto mapToDto(Store store) {
        return StoreDto.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .storeAdminId(store.getStoreAdmin() != null ? store.getStoreAdmin().getId() : null)
                .storeAdmin(UserMapper.toDTO(store.getStoreAdmin()))
                .storeType(store.getStoreType())
                .description(store.getDescription())
                .contact(store.getContact())
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .status(store.getStatus())
                .build();
    }

    public static Store mapToEntity(StoreDto dto, User storeAdmin) {
        return Store.builder()
                .id(dto.getId())
                .storeName(dto.getStoreName())
                .storeAdmin(storeAdmin)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .storeType(dto.getStoreType())
                .description(dto.getDescription())
                .build();
    }
}
