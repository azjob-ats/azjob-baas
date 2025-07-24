
package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateGroupRequestDTO {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private UUID enterpriseId;
}
