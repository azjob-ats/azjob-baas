package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserHasPermissionRequestDTO {
    @NotNull
    private UUID userId;
    @NotBlank
    private String actionName;
    @NotNull
    private UUID enterpriseId;
}
