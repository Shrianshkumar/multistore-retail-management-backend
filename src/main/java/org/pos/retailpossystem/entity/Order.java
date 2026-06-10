package org.pos.retailpossystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.pos.retailpossystem.domain.enums.OrderStatus;
import org.pos.retailpossystem.domain.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double totalAmount;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Branch branch;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private User cashier;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.COMPLETED;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}