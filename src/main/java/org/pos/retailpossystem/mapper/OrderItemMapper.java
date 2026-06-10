package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.OrderItem;
import org.pos.retailpossystem.payload.dto.OrderItemDto;

public class OrderItemMapper {

    public static OrderItemDto mapToDto(OrderItem item) {

        if (item == null) {
            return null;
        }

        return OrderItemDto.builder()
                .id(item.getId())
                .productId(
                        item.getProduct() != null
                                ? item.getProduct().getId()
                                : null
                )
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .product(
                        item.getProduct() != null
                                ? ProductMapper.mapToDto(item.getProduct())
                                : null
                )
                .build();
    }
}
