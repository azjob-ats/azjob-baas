package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class GrantPermissionToRoleRequestDTO {
    @NotNull
    private UUID roleId;
    @NotNull
    private UUID actionId;
    @NotNull
    private UUID enterpriseId;
}
