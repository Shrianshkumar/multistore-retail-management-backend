package org.pos.retailpossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private Double mrp;
    private Double salePrice;
    private String brand;
    private Long categoryId;
    private String category;
    private Long storeId;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
