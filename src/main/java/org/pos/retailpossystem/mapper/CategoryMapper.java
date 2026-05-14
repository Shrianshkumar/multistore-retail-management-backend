package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.Category;
import org.pos.retailpossystem.payload.dto.CategoryDto;

public class CategoryMapper {

    public static CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .storeId(category.getStore().getId())
                .build();
    }
}
