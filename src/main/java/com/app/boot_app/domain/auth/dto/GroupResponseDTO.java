package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Response DTO for group details")
public class GroupResponseDTO {
    @Schema(description = "Unique identifier of the group", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "Name of the group", example = "Finance Department")
    private String name;
    @Schema(description = "Description of the group", example = "Handles all financial operations")
    private String description;
    @Schema(description = "ID of the default role for new members in this group", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID defaultRoleId;
    @Schema(description = "Timestamp when the group was created", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp when the group was last updated", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime updatedAt;
    @Schema(description = "ID of the user who created the group", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID createdBy;
    @Schema(description = "Whether the group is active", example = "true")
    private Boolean isActive;
}
