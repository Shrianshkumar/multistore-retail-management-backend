package org.pos.retailpossystem.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreContactInfo {

    private String address;

    private String phone;

    @Email(message = "Invalid email format")
    private String email;
}
