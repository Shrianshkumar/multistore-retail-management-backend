package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;
import org.pos.retailpossystem.entity.Category;
import org.pos.retailpossystem.entity.Product;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.AccessDeniedException;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.ProductMapper;
import org.pos.retailpossystem.payload.dto.ProductDto;
import org.pos.retailpossystem.repository.CategoryRepo;
import org.pos.retailpossystem.repository.ProductRepo;
import org.pos.retailpossystem.repository.StoreRepo;
import org.pos.retailpossystem.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final StoreRepo storeRepo;
    private final CategoryRepo categoryRepo;

    @Override
    public ProductDto createProduct(ProductDto productDto, User user) {

        Store store = storeRepo.findById(productDto.getStoreId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Store not found"));

        validateAuthority(store, user);

        Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        Product product = ProductMapper.mapToEntity(
                productDto,
                store,
                category
        );

        return ProductMapper.mapToDto(
                productRepo.save(product)
        );
    }

    @Override
    public ProductDto getProductById(Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return ProductMapper.mapToDto(product);
    }

    @Override
    public ProductDto updateProduct(
            Long productId,
            ProductDto productDto,
            User user) {

        Product existing = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        validateAuthority(existing.getStore(), user);

        existing.setName(productDto.getName());
        existing.setSku(productDto.getSku());
        existing.setDescription(productDto.getDescription());
        existing.setMrp(productDto.getMrp());
        existing.setSalePrice(productDto.getSalePrice());
        existing.setBrand(productDto.getBrand());
        existing.setImagePath(productDto.getImagePath());
        existing.setCategory(category);

        if (productDto.getStoreId() != null) {

            Store store = storeRepo.findById(productDto.getStoreId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Store not found"));

            existing.setStore(store);
        }

        return ProductMapper.mapToDto(
                productRepo.save(existing)
        );
    }

    @Override
    public void deleteProduct(Long productId, User user) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        validateAuthority(product.getStore(), user);

        productRepo.delete(product);
    }

    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) {

        return productRepo.findByStoreId(storeId)
                .stream()
                .map(ProductMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProducts(
            Long storeId,
            String query) {

        return productRepo.searchProducts(storeId, query)
                .stream()
                .map(ProductMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private void validateAuthority(Store store, User user) {

        boolean isStoreManager =
                user.getRole() == UserRole.ROLE_STORE_MANAGER
                        && user.getStore() != null
                        && user.getStore().getId().equals(store.getId());

        boolean isStoreAdmin =
                user.getRole() == UserRole.ROLE_STORE_ADMIN
                        && store.getStoreAdmin().getId().equals(user.getId());

        if (isStoreManager || isStoreAdmin) {
            return;
        }

        throw new AccessDeniedException(
                "You are not authorized to manage this store."
        );
    }
}