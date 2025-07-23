package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateEnterpriseRequestDTO {
    @NotBlank
    private String nameEnterprise;
    @NotNull
    private UUID ownerId;
}
