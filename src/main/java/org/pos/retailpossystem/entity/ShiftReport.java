package org.pos.retailpossystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.pos.retailpossystem.payload.dto.OrderDto;
import org.pos.retailpossystem.payload.dto.PaymentInfoDto;
import org.pos.retailpossystem.payload.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shift_reports")
public class ShiftReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;

    private Double totalSales;
    private Double totalRefunds;
    private Double netSales;

    private Integer totalOrders;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User cashier;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Branch branch;

    @Transient
    private List<PaymentInfoDto> paymentInformation;

    @Transient
    private List<ProductDto> topSellingProducts;

    @Transient
    private List<OrderDto> recentOrders;

    @OneToMany(
            mappedBy = "shiftReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Refund> refunds;
}

