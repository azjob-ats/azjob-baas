package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePermissionRequestDTO {
    @NotNull
    private UUID idRole;
    @NotNull
    private UUID idAction;
    @NotNull
    private Boolean allowed;
    @NotNull
    private UUID idEnterprise;
    private UUID idGroup;
}
