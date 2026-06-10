package org.pos.retailpossystem.service;

import org.pos.retailpossystem.domain.enums.OrderStatus;
import org.pos.retailpossystem.domain.enums.PaymentMethod;
import org.pos.retailpossystem.payload.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> searchOrders(
            Long branchId,
            Long customerId,
            Long cashierId,
            PaymentMethod paymentMethod,
            OrderStatus status
    );

    List<OrderDto> getOrdersByCashier(Long cashierId);

    void deleteOrder(Long orderId);

    List<OrderDto> getTodayOrdersByBranch(Long branchId);

    List<OrderDto> getOrdersByCustomer(Long customerId);

    List<OrderDto> getRecentOrdersByBranch(Long branchId);
}
