package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.OrderStatus;
import org.pos.retailpossystem.domain.enums.PaymentMethod;
import org.pos.retailpossystem.entity.*;
import org.pos.retailpossystem.exception.BadRequestException;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.CustomerMapper;
import org.pos.retailpossystem.mapper.OrderMapper;
import org.pos.retailpossystem.payload.dto.OrderDto;
import org.pos.retailpossystem.repository.BranchRepo;
import org.pos.retailpossystem.repository.OrderRepo;
import org.pos.retailpossystem.repository.ProductRepo;
import org.pos.retailpossystem.service.OrderService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final BranchRepo branchRepo;
    private final UserService userService;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        User cashier = userService.getCurrentUser();

        Branch branch = cashier.getBranch();

        if (branch == null) {
            throw new BadRequestException(
                    "Cashier is not assigned to any branch"
            );
        }

        Order order = Order.builder()
                .branch(branch)
                .cashier(cashier)
                .customer(
                        orderDto.getCustomer() != null
                                ? CustomerMapper.mapToEntity(
                                orderDto.getCustomer()
                        )
                                : null
                )
                .paymentMethod(orderDto.getPaymentMethod())
                .status(OrderStatus.COMPLETED)
                .build();

        List<OrderItem> orderItems =
                orderDto.getItems().stream()
                        .map(itemDto -> {

                            Product product = productRepo.findById(
                                            itemDto.getProductId()
                                    )
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
                                                    "Product not found"
                                            ));

                            return OrderItem.builder()
                                    .product(product)
                                    .quantity(itemDto.getQuantity())
                                    .price(
                                            product.getSalePrice()
                                                    * itemDto.getQuantity()
                                    )
                                    .order(order)
                                    .build();
                        })
                        .collect(Collectors.toList());

        double totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        return OrderMapper.mapToDto(
                orderRepo.save(order)
        );
    }

    @Override
    public OrderDto getOrderById(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));

        return OrderMapper.mapToDto(order);
    }

    @Override
    public List<OrderDto> searchOrders(
            Long branchId,
            Long customerId,
            Long cashierId,
            PaymentMethod paymentMethod,
            OrderStatus status) {

        return orderRepo.findByBranchId(branchId)
                .stream()

                .filter(order ->
                        customerId == null
                                || (
                                order.getCustomer() != null
                                        && order.getCustomer()
                                        .getId()
                                        .equals(customerId)
                        )
                )

                .filter(order ->
                        cashierId == null
                                || (
                                order.getCashier() != null
                                        && order.getCashier()
                                        .getId()
                                        .equals(cashierId)
                        )
                )

                .filter(order ->
                        paymentMethod == null
                                || order.getPaymentMethod()
                                == paymentMethod
                )

                .filter(order ->
                        status == null
                                || order.getStatus()
                                == status
                )

                .sorted(
                        Comparator.comparing(
                                Order::getCreatedAt
                        ).reversed()
                )

                .map(OrderMapper::mapToDto)

                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCashier(
            Long cashierId) {

        return orderRepo.findByCashierId(cashierId)
                .stream()
                .map(OrderMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));

        orderRepo.delete(order);
    }

    @Override
    public List<OrderDto> getTodayOrdersByBranch(
            Long branchId) {

        LocalDate today = LocalDate.now();

        LocalDateTime start =
                today.atStartOfDay();

        LocalDateTime end =
                today.plusDays(1).atStartOfDay();

        return orderRepo.findByBranchIdAndCreatedAtBetween(
                        branchId,
                        start,
                        end
                )
                .stream()
                .map(OrderMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomer(
            Long customerId) {

        return orderRepo.findByCustomerId(customerId)
                .stream()
                .map(OrderMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getRecentOrdersByBranch(
            Long branchId) {

        branchRepo.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Branch not found"
                        ));

        return orderRepo
                .findTop5ByBranchIdOrderByCreatedAtDesc(
                        branchId
                )
                .stream()
                .map(OrderMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
