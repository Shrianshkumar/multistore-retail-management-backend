package org.pos.retailpossystem.payload.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShiftReportDto {
    private Long id;
    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;
    private double totalSales;
    private double totalRefunds;
    private double netSales;
    private int totalOrders;
    private UserDto cashier;
    private Long cashierId;
    private Long branchId;
    private List<OrderDto> recentOrders;
    private List<ProductDto> topSellingProducts;
    private List<RefundDto> refunds;
    private List<PaymentInfoDto> paymentInformation;
}
