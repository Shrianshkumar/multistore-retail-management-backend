package org.pos.retailpossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pos.retailpossystem.domain.enums.OrderStatus;
import org.pos.retailpossystem.domain.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Double totalAmount;
    private Long branchId;
    private Long cashierId;
    private CustomerDto customer;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
}
