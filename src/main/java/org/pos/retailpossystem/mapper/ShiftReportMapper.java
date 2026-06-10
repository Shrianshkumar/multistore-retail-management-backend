package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.Refund;
import org.pos.retailpossystem.entity.ShiftReport;
import org.pos.retailpossystem.payload.dto.RefundDto;
import org.pos.retailpossystem.payload.dto.ShiftReportDto;

import java.util.List;

public class ShiftReportMapper {

    public static ShiftReportDto mapToDto(
            ShiftReport shiftReport
    ) {

        if (shiftReport == null) {
            return null;
        }

        return ShiftReportDto.builder()
                .id(shiftReport.getId())
                .shiftStart(shiftReport.getShiftStart())
                .shiftEnd(shiftReport.getShiftEnd())

                .totalSales(
                        shiftReport.getTotalSales() != null
                                ? shiftReport.getTotalSales()
                                : 0.0
                )

                .totalRefunds(
                        shiftReport.getTotalRefunds() != null
                                ? shiftReport.getTotalRefunds()
                                : 0.0
                )

                .netSales(
                        shiftReport.getNetSales() != null
                                ? shiftReport.getNetSales()
                                : 0.0
                )

                .totalOrders(
                        shiftReport.getTotalOrders() != null
                                ? shiftReport.getTotalOrders()
                                : 0
                )

                .cashier(
                        shiftReport.getCashier() != null
                                ? UserMapper.mapToDto(
                                shiftReport.getCashier()
                        )
                                : null
                )

                .cashierId(
                        shiftReport.getCashier() != null
                                ? shiftReport.getCashier().getId()
                                : null
                )

                .branchId(
                        shiftReport.getBranch() != null
                                ? shiftReport.getBranch().getId()
                                : null
                )

                .recentOrders(
                        shiftReport.getRecentOrders() != null
                                ? shiftReport.getRecentOrders()
                                : List.of()
                )

                .topSellingProducts(
                        shiftReport.getTopSellingProducts() != null
                                ? shiftReport.getTopSellingProducts()
                                : List.of()
                )

                .refunds(
                        mapRefunds(
                                shiftReport.getRefunds()
                        )
                )

                .paymentInformation(
                        shiftReport.getPaymentInformation() != null
                                ? shiftReport.getPaymentInformation()
                                : List.of()
                )

                .build();
    }

    private static List<RefundDto> mapRefunds(
            List<Refund> refunds
    ) {

        if (refunds == null) {
            return List.of();
        }

        return refunds.stream()
                .map(RefundMapper::mapToDto)
                .toList();
    }
}