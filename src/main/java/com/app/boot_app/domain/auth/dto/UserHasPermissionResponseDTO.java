package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response DTO indicating if a user has a specific permission")
public class UserHasPermissionResponseDTO {
    @Schema(description = "True if the user has the permission, false otherwise.", example = "true")
    private boolean hasPermission;
}
