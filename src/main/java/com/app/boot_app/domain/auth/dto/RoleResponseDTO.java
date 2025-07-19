package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Response DTO for role details")
public class RoleResponseDTO {
    @Schema(description = "Unique identifier of the role", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "Name of the role (e.g., ADMIN, USER)", example = "ADMIN")
    private String name;
    @Schema(description = "Description of the role's permissions", example = "Administrator role with full access")
    private String description;
    @Schema(description = "List of permissions associated with this role", example = "[\"users:read\", \"users:write\"]")
    private List<String> permissions;
    @Schema(description = "Whether this role is assigned to new users by default", example = "false")
    private Boolean isDefault;
    @Schema(description = "Hierarchical level of the role (1-100)", example = "100")
    private Integer level;
    @Schema(description = "Timestamp when the role was created", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp when the role was last updated", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime updatedAt;
}
