package org.pos.retailpossystem.service;

import org.pos.retailpossystem.payload.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto dto);
    List<CategoryDto> getCategoriesByStoreId(Long storeId);
    CategoryDto updateCategory(Long id, CategoryDto dto);
    void deleteCategory(Long id);
}
