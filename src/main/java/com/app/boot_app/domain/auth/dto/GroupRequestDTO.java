package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO for creating or updating a group")
public class GroupRequestDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    @Schema(description = "Name of the group", example = "Finance Department")
    private String name;

    @Size(max = 1000)
    @Schema(description = "Description of the group", example = "Handles all financial operations")
    private String description;

    @Schema(description = "ID of the default role for new members in this group", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID defaultRoleId;
}
