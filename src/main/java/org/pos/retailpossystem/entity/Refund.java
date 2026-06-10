package org.pos.retailpossystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.pos.retailpossystem.domain.enums.PaymentMethod;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    private String reason;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JsonIgnore
    private ShiftReport shiftReport;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User cashier;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Branch branch;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
