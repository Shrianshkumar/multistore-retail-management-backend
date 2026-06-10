package org.pos.retailpossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pos.retailpossystem.domain.enums.PaymentMethod;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundDto {
    private Long id;
    private Long orderId;
    private String reason;
    private Double amount;
    private String cashierName;
    private Long shiftReportId;
    private Long branchId;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
}
