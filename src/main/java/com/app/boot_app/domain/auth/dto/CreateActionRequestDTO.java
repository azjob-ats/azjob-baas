package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateActionRequestDTO {
    @NotBlank
    private String name;
}
