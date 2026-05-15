package org.pos.retailpossystem.service;

import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.payload.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, User user);
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long productId,
                             ProductDto productDto,
                             User user);

    void deleteProduct(Long productId, User user);

    List<ProductDto> getProductsByStoreId(Long storeId);

    List<ProductDto> searchProducts(Long storeId, String query);
}
