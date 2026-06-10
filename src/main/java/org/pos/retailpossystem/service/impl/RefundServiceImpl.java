package org.pos.retailpossystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.pos.retailpossystem.domain.enums.OrderStatus;
import org.pos.retailpossystem.entity.*;
import org.pos.retailpossystem.exception.ResourceNotFoundException;
import org.pos.retailpossystem.mapper.RefundMapper;
import org.pos.retailpossystem.payload.dto.RefundDto;
import org.pos.retailpossystem.repository.BranchRepo;
import org.pos.retailpossystem.repository.OrderRepo;
import org.pos.retailpossystem.repository.RefundRepo;
import org.pos.retailpossystem.repository.ShiftReportRepo;
import org.pos.retailpossystem.service.RefundService;
import org.pos.retailpossystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundServiceImpl implements RefundService {

    private final RefundRepo refundRepo;
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final BranchRepo branchRepo;
    private final ShiftReportRepo shiftReportRepo;

    @Override
    public RefundDto createRefund(RefundDto refundDto) {

        User currentCashier = userService.getCurrentUser();

        Order order = orderRepo.findById(refundDto.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        Branch branch = branchRepo.findById(refundDto.getBranchId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found"));

        ShiftReport shiftReport = null;

        if (refundDto.getShiftReportId() != null) {

            shiftReport = shiftReportRepo
                    .findById(refundDto.getShiftReportId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Shift report not found"));
        }

        Refund refund = RefundMapper.mapToEntity(
                refundDto,
                order,
                currentCashier,
                branch,
                shiftReport
        );

        refund.setAmount(order.getTotalAmount());

        Refund savedRefund = refundRepo.save(refund);

        order.setStatus(OrderStatus.REFUNDED);
        orderRepo.save(order);

        return RefundMapper.mapToDto(savedRefund);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDto> getAllRefunds() {

        return refundRepo.findAll()
                .stream()
                .map(RefundMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDto> getRefundsByCashier(Long cashierId) {

        return refundRepo.findByCashierId(cashierId)
                .stream()
                .map(RefundMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDto> getRefundsByShiftReport(Long shiftReportId) {

        return refundRepo.findByShiftReportId(shiftReportId)
                .stream()
                .map(RefundMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDto> getRefundsByCashierAndDateRange(
            Long cashierId,
            LocalDateTime from,
            LocalDateTime to
    ) {

        return refundRepo
                .findByCashierIdAndCreatedAtBetween(cashierId, from, to)
                .stream()
                .map(RefundMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDto> getRefundsByBranch(Long branchId) {

        return refundRepo.findByBranchId(branchId)
                .stream()
                .map(RefundMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDto> getRecentRefundsByBranch(Long branchId) {

        return refundRepo
                .findTop5ByBranchIdOrderByCreatedAtDesc(branchId)
                .stream()
                .map(RefundMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RefundDto getRefundById(Long refundId) {

        Refund refund = refundRepo.findById(refundId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Refund not found"));

        return RefundMapper.mapToDto(refund);
    }

    @Override
    public void deleteRefund(Long refundId) {

        Refund refund = refundRepo.findById(refundId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Refund not found"));

        refundRepo.delete(refund);
    }
}
