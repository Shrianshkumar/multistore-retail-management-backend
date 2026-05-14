package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.Category;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.AccessDeniedException;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.CategoryMapper;
import org.pos.retailpossystem.payload.dto.CategoryDto;
import org.pos.retailpossystem.repository.CategoryRepo;
import org.pos.retailpossystem.repository.StoreRepo;
import org.pos.retailpossystem.service.CategoryService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final StoreRepo storeRepo;
    private final UserService userService;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {

        User user = userService.getCurrentUser();

        Store store = storeRepo.findById(dto.getStoreId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Store not found"));

        validateUserAuthority(user, store);

        Category category = Category.builder()
                .name(dto.getName())
                .store(store)
                .build();

        return CategoryMapper.mapToDto(categoryRepo.save(category));
    }

    @Override
    public List<CategoryDto> getCategoriesByStoreId(Long storeId) {
        return categoryRepo.getByStoreId(storeId).stream()
                .map(CategoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        User user = userService.getCurrentUser();
        validateUserAuthority(user, category.getStore());

        category.setName(dto.getName());
        return CategoryMapper.mapToDto(categoryRepo.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        User user = userService.getCurrentUser();
        validateUserAuthority(user, category.getStore());

        categoryRepo.delete(category);
    }

    private void validateUserAuthority(User user, Store store) {
        boolean isAdmin =
                user.getRole().equals(UserRole.ROLE_STORE_ADMIN);

        boolean isManager =
                user.getRole().equals(UserRole.ROLE_STORE_MANAGER);

        boolean isSameStore =
                user.getId().equals(store.getStoreAdmin().getId());

        if ((isAdmin && isSameStore) || isManager) {
            return;
        }

        throw new AccessDeniedException(
                "You do not have permission to manage this category."
        );
    }
}
