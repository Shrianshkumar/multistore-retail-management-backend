package org.pos.retailpossystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.exception.AccessDeniedException;
import org.pos.retailpossystem.exception.UserException;
import org.pos.retailpossystem.payload.dto.ProductDto;
import org.pos.retailpossystem.service.ProductService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ProductDto> create(
            @Valid @RequestBody ProductDto dto,
            @RequestHeader("Authorization") String jwt
    ) throws UserException, AccessDeniedException {
        User user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(productService.createProduct(dto, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id,
                                             @RequestBody ProductDto dto,

                                             @RequestHeader("Authorization") String jwt) throws UserException, AccessDeniedException {
        User user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(productService.updateProduct(id, dto,user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestHeader("Authorization") String jwt) throws UserException, AccessDeniedException {
        User user = userService.getUserFromJwtToken(jwt);
        productService.deleteProduct(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDto>> getByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(productService.getProductsByStoreId(storeId));
    }

    @GetMapping("/store/{storeId}/search")
    public ResponseEntity<List<ProductDto>> searchProductsByStore(
            @PathVariable Long storeId,
            @RequestParam String q) {
        return ResponseEntity.ok(productService.searchProducts(storeId,q));
    }



}

