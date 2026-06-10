package org.pos.retailpossystem.controller;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.OrderStatus;
import org.pos.retailpossystem.domain.enums.PaymentMethod;
import org.pos.retailpossystem.payload.dto.OrderDto;
import org.pos.retailpossystem.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Create Order
    @PostMapping
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_BRANCH_CASHIER',
                'ROLE_BRANCH_MANAGER'
            )
            """)
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody OrderDto orderDto) {

        return ResponseEntity.ok(
                orderService.createOrder(orderDto)
        );
    }

    // Get Order By orderId
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }

    // Search Orders By Branch
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<OrderDto>> searchOrders(
            @PathVariable Long branchId,

            @RequestParam(required = false)
            Long customerId,

            @RequestParam(required = false)
            Long cashierId,

            @RequestParam(required = false)
            PaymentMethod paymentMethod,

            @RequestParam(required = false)
            OrderStatus status) {

        return ResponseEntity.ok(
                orderService.searchOrders(
                        branchId,
                        customerId,
                        cashierId,
                        paymentMethod,
                        status
                )
        );
    }

    // Get Orders By Cashier
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCashier(
            @PathVariable Long cashierId) {

        return ResponseEntity.ok(
                orderService.getOrdersByCashier(cashierId)
        );
    }

    // Get Today's Orders By Branch
    @GetMapping("/today/branch/{branchId}")
    public ResponseEntity<List<OrderDto>> getTodayOrdersByBranch(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                orderService.getTodayOrdersByBranch(branchId)
        );
    }

    // Get Orders By Customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                orderService.getOrdersByCustomer(customerId)
        );
    }

    // Get Recent Orders
    @GetMapping("/recent/{branchId}")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_BRANCH_MANAGER',
                'ROLE_STORE_ADMIN',
                'ROLE_STORE_MANAGER'
            )
            """)
    public ResponseEntity<List<OrderDto>> getRecentOrdersByBranch(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                orderService.getRecentOrdersByBranch(branchId)
        );
    }

    // Delete Order
    @DeleteMapping("/{orderId}")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_STORE_MANAGER',
                'ROLE_STORE_ADMIN'
            )
            """)
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long orderId) {

        orderService.deleteOrder(orderId);

        return ResponseEntity.noContent().build();
    }
}
