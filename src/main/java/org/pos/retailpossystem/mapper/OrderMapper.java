package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.Order;
import org.pos.retailpossystem.payload.dto.OrderDto;

import java.util.Collections;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto mapToDto(Order order) {

        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .branchId(
                        order.getBranch() != null
                                ? order.getBranch().getId()
                                : null
                )
                .cashierId(
                        order.getCashier() != null
                                ? order.getCashier().getId()
                                : null
                )
                .customer(
                        order.getCustomer() != null
                                ? CustomerMapper.mapToDto(order.getCustomer())
                                : null
                )
                .createdAt(order.getCreatedAt())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .items(
                        order.getItems() != null
                                ? order.getItems().stream()
                                  .map(OrderItemMapper::mapToDto)
                                  .collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .build();
    }
}
