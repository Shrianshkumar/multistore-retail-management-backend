package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.Category;
import org.pos.retailpossystem.entity.Product;
import org.pos.retailpossystem.entity.Store;
import org.pos.retailpossystem.payload.dto.ProductDto;

public class ProductMapper {

    public static ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .mrp(product.getMrp())
                .salePrice(product.getSalePrice())
                .brand(product.getBrand())
                .category(product.getCategory().getName())
                .categoryId(product.getCategory().getId())
                .storeId(product.getStore() != null ? product.getStore().getId() : null)
                .imagePath(product.getImagePath())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static Product mapToEntity(ProductDto dto,
                                   Store store,
                                   Category category) {
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .sku(dto.getSku())
                .description(dto.getDescription())
                .mrp(dto.getMrp())
                .salePrice(dto.getSalePrice())
                .brand(dto.getBrand())
                .category(category)

                .store(store)
                .imagePath(dto.getImagePath())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
