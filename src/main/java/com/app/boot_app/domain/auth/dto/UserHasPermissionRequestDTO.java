package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO to check if a user has a specific permission")
public class UserHasPermissionRequestDTO {
    @NotNull
    @Schema(description = "The unique identifier of the user.", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID userId;
    @NotBlank
    @Schema(description = "The unique identifier of the action to check.", example = "d1e2f3g4-h5i6-7890-1234-567890abcdef")
    private UUID actionId;
    @NotNull
    @Schema(description = "The unique identifier of the enterprise.", example = "c1d2e3f4-g5h6-7890-1234-567890abcdef")
    private UUID enterpriseId;
}
