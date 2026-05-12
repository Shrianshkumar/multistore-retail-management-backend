package org.pos.retailpossystem.payload.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pos.retailpossystem.domain.enums.UserRole;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String fullName;
    private UserRole role;
    private String username;
    private Long storeId;
    private Long branchId;
    private BranchDto branch;
    private String branchName;
    private LocalDateTime lastLogin;



    public UserDto(Long id, String email, String fullName,
                   UserRole role, String branchName,
                   LocalDateTime lastLogin) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.password = null;
        this.phone = null;
        this.username = null;
        this.storeId = null;
        this.branchId = null;
        this.branch = null;
        this.branchName=branchName;
        this.lastLogin=lastLogin;

    }
}
