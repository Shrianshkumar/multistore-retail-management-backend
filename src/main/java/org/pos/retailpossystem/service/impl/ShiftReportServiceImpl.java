package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.PaymentMethod;
import org.pos.retailpossystem.entity.*;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.OrderMapper;
import org.pos.retailpossystem.mapper.ProductMapper;
import org.pos.retailpossystem.mapper.ShiftReportMapper;
import org.pos.retailpossystem.payload.dto.OrderDto;
import org.pos.retailpossystem.payload.dto.PaymentInfoDto;
import org.pos.retailpossystem.payload.dto.ProductDto;
import org.pos.retailpossystem.payload.dto.ShiftReportDto;
import org.pos.retailpossystem.repository.BranchRepo;
import org.pos.retailpossystem.repository.OrderRepo;
import org.pos.retailpossystem.repository.RefundRepo;
import org.pos.retailpossystem.repository.ShiftReportRepo;
import org.pos.retailpossystem.service.ShiftReportService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftReportServiceImpl implements ShiftReportService {

    private final ShiftReportRepo shiftReportRepo;
    private final BranchRepo branchRepo;
    private final OrderRepo orderRepo;
    private final RefundRepo refundRepo;
    private final UserService userService;

    @Override
    public ShiftReportDto startShift(
            Long cashierId,
            Long branchId
    ) {

        User currentUser = userService.getCurrentUser();

        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found"));

        LocalDateTime shiftStart = LocalDateTime.now();

        LocalDateTime startOfDay = shiftStart
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        LocalDateTime endOfDay = shiftStart
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        Optional<ShiftReport> existingShift =
                shiftReportRepo.findByCashierIdAndShiftStartBetween(
                        currentUser.getId(),
                        startOfDay,
                        endOfDay
                );

        if (existingShift.isPresent()) {
            throw new RuntimeException("Shift already started today");
        }

        ShiftReport shiftReport = ShiftReport.builder()
                .cashier(currentUser)
                .branch(branch)
                .shiftStart(shiftStart)
                .build();

        ShiftReport savedShift =
                shiftReportRepo.save(shiftReport);

        return ShiftReportMapper.mapToDto(savedShift);
    }

    @Override
    public ShiftReportDto endShift(
            Long shiftReportId
    ) {

        User currentUser = userService.getCurrentUser();

        ShiftReport shiftReport =
                shiftReportRepo
                        .findTopByCashierIdAndShiftEndIsNullOrderByShiftStartDesc(
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No active shift found"
                                ));

        LocalDateTime shiftEnd = LocalDateTime.now();

        shiftReport.setShiftEnd(shiftEnd);

        List<Order> orders =
                orderRepo.findByCashierIdAndCreatedAtBetween(
                        currentUser.getId(),
                        shiftReport.getShiftStart(),
                        shiftEnd
                );

        List<Refund> refunds =
                refundRepo.findByCashierIdAndCreatedAtBetween(
                        currentUser.getId(),
                        shiftReport.getShiftStart(),
                        shiftEnd
                );

        double totalSales = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        double totalRefunds = refunds.stream()
                .mapToDouble(refund ->
                        refund.getAmount() != null
                                ? refund.getAmount()
                                : 0.0
                )
                .sum();

        int totalOrders = orders.size();

        double netSales = totalSales - totalRefunds;

        shiftReport.setTotalSales(totalSales);
        shiftReport.setTotalRefunds(totalRefunds);
        shiftReport.setNetSales(netSales);
        shiftReport.setTotalOrders(totalOrders);

        shiftReport.setRecentOrders(
                getRecentOrders(orders)
        );

        shiftReport.setTopSellingProducts(
                getTopSellingProducts(orders)
        );

        shiftReport.setPaymentInformation(
                getPaymentInformation(orders, totalSales)
        );

        shiftReport.setRefunds(refunds);

        ShiftReport savedShift =
                shiftReportRepo.save(shiftReport);

        return ShiftReportMapper.mapToDto(savedShift);
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftReportDto getShiftReportById(
            Long shiftReportId
    ) {

        ShiftReport shiftReport =
                shiftReportRepo.findById(shiftReportId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shift report not found"
                                ));

        return ShiftReportMapper.mapToDto(shiftReport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftReportDto> getAllShiftReports() {

        return shiftReportRepo.findAll()
                .stream()
                .map(ShiftReportMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftReportDto> getShiftReportsByCashier(
            Long cashierId
    ) {

        return shiftReportRepo.findByCashierId(cashierId)
                .stream()
                .map(ShiftReportMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftReportDto getCurrentShiftProgress(
            Long cashierId
    ) {

        User currentUser = userService.getCurrentUser();

        ShiftReport shiftReport =
                shiftReportRepo
                        .findTopByCashierIdAndShiftEndIsNullOrderByShiftStartDesc(
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No active shift found"
                                ));

        LocalDateTime now = LocalDateTime.now();

        List<Order> orders =
                orderRepo.findByCashierIdAndCreatedAtBetween(
                        currentUser.getId(),
                        shiftReport.getShiftStart(),
                        now
                );

        List<Refund> refunds =
                refundRepo.findByCashierIdAndCreatedAtBetween(
                        currentUser.getId(),
                        shiftReport.getShiftStart(),
                        now
                );

        double totalSales = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        double totalRefunds = refunds.stream()
                .mapToDouble(refund ->
                        refund.getAmount() != null
                                ? refund.getAmount()
                                : 0.0
                )
                .sum();

        int totalOrders = orders.size();

        double netSales = totalSales - totalRefunds;

        shiftReport.setTotalSales(totalSales);
        shiftReport.setTotalRefunds(totalRefunds);
        shiftReport.setNetSales(netSales);
        shiftReport.setTotalOrders(totalOrders);

        shiftReport.setRecentOrders(
                getRecentOrders(orders)
        );

        shiftReport.setTopSellingProducts(
                getTopSellingProducts(orders)
        );

        shiftReport.setPaymentInformation(
                getPaymentInformation(orders, totalSales)
        );

        shiftReport.setRefunds(refunds);

        return ShiftReportMapper.mapToDto(shiftReport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftReportDto> getShiftReportsByBranch(
            Long branchId
    ) {

        return shiftReportRepo.findByBranchId(branchId)
                .stream()
                .map(ShiftReportMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftReportDto getShiftReportByCashierAndDate(
            Long cashierId,
            LocalDateTime date
    ) {

        LocalDateTime start = date
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        LocalDateTime end = date
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        ShiftReport shiftReport =
                shiftReportRepo
                        .findByCashierIdAndShiftStartBetween(
                                cashierId,
                                start,
                                end
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shift report not found"
                                ));

        return ShiftReportMapper.mapToDto(shiftReport);
    }

    @Override
    public void deleteShiftReport(
            Long shiftReportId
    ) {

        ShiftReport shiftReport =
                shiftReportRepo.findById(shiftReportId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shift report not found"
                                ));

        shiftReportRepo.delete(shiftReport);
    }

    // ---------------- HELPER METHODS ----------------

    private List<OrderDto> getRecentOrders(
            List<Order> orders
    ) {

        return orders.stream()
                .sorted(
                        Comparator.comparing(Order::getCreatedAt)
                                .reversed()
                )
                .limit(5)
                .map(OrderMapper::mapToDto)
                .toList();
    }

    private List<ProductDto> getTopSellingProducts(
            List<Order> orders
    ) {

        Map<Product, Integer> productSalesMap =
                new HashMap<>();

        for (Order order : orders) {

            for (OrderItem item : order.getItems()) {

                Product product = item.getProduct();

                productSalesMap.put(
                        product,
                        productSalesMap.getOrDefault(
                                product,
                                0
                        ) + item.getQuantity()
                );
            }
        }

        return productSalesMap.entrySet()
                .stream()
                .sorted((a, b) ->
                        b.getValue().compareTo(a.getValue())
                )
                .limit(5)
                .map(Map.Entry::getKey)
                .map(ProductMapper::mapToDto)
                .toList();
    }

    private List<PaymentInfoDto> getPaymentInformation(
            List<Order> orders,
            double totalSales
    ) {

        Map<PaymentMethod, List<Order>> groupedOrders =
                orders.stream()
                        .collect(
                                Collectors.groupingBy(
                                        order ->
                                                order.getPaymentMethod() != null
                                                        ? order.getPaymentMethod()
                                                        : PaymentMethod.CASH
                                )
                        );

        List<PaymentInfoDto> paymentInformation =
                new ArrayList<>();

        for (Map.Entry<PaymentMethod, List<Order>> entry
                : groupedOrders.entrySet()) {

            double amount = entry.getValue()
                    .stream()
                    .mapToDouble(Order::getTotalAmount)
                    .sum();

            int transactionCount =
                    entry.getValue().size();

            double percentage =
                    totalSales == 0
                            ? 0
                            : (amount / totalSales) * 100;

            PaymentInfoDto paymentInfoDto =
                    new PaymentInfoDto();

            paymentInfoDto.setPaymentMethod(
                    entry.getKey()
            );

            paymentInfoDto.setTotalAmount(
                    amount
            );

            paymentInfoDto.setTransactionCount(
                    transactionCount
            );

            paymentInfoDto.setPercentage(
                    percentage
            );

            paymentInformation.add(paymentInfoDto);
        }

        return paymentInformation;
    }
}
