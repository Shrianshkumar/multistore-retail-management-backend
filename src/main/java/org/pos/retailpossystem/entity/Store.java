package org.pos.retailpossystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.pos.retailpossystem.domain.enums.StoreStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "brand name is required")
    private String brand;

    @OneToOne
    private User storeAdmin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;

    private String storeType;

    private StoreStatus status;

    // Contact Information
    @Embedded
    private StoreContactInfo contact=new StoreContactInfo();

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        status=StoreStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
