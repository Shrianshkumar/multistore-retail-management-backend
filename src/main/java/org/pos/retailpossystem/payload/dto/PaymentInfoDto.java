package org.pos.retailpossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pos.retailpossystem.domain.enums.PaymentMethod;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDto {
    private PaymentMethod paymentMethod;
    private Double totalAmount;
    private Integer transactionCount;
    private Double percentage;
}
