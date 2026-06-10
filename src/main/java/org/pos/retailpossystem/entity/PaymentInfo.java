package org.pos.retailpossystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pos.retailpossystem.domain.enums.PaymentMethod;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
    private PaymentMethod paymentMethod;
    private Double totalAmount;
    private int transactionCount;
    private double percentage;
}
