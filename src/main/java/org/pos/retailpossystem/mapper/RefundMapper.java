package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.*;
import org.pos.retailpossystem.payload.dto.RefundDto;

public class RefundMapper {

    public static RefundDto mapToDto(Refund refund) {

        if (refund == null) {
            return null;
        }

        return RefundDto.builder()
                .id(refund.getId())
                .orderId(
                        refund.getOrder() != null
                                ? refund.getOrder().getId()
                                : null
                )
                .reason(refund.getReason())
                .amount(refund.getAmount())
                .cashierName(
                        refund.getCashier() != null
                                ? refund.getCashier().getFullName()
                                : null
                )
                .branchId(
                        refund.getBranch() != null
                                ? refund.getBranch().getId()
                                : null
                )
                .shiftReportId(
                        refund.getShiftReport() != null
                                ? refund.getShiftReport().getId()
                                : null
                )
                .createdAt(refund.getCreatedAt())
                .paymentMethod(refund.getPaymentMethod())
                .build();
    }

    public static Refund mapToEntity(
            RefundDto refundDto,
            Order order,
            User cashier,
            Branch branch,
            ShiftReport shiftReport
    ) {

        if (refundDto == null) {
            return null;
        }

        return Refund.builder()
                .id(refundDto.getId())
                .order(order)
                .reason(refundDto.getReason())
                .amount(refundDto.getAmount())
                .cashier(cashier)
                .branch(branch)
                .shiftReport(shiftReport)
                .paymentMethod(refundDto.getPaymentMethod())
                .createdAt(refundDto.getCreatedAt())
                .build();
    }
}
