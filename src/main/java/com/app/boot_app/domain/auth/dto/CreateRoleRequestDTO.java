package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleRequestDTO {
    @NotBlank
    private String name;
    private String description;
}
