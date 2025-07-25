package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO for granting a permission to a role")
public class GrantPermissionToRoleRequestDTO {
    @NotNull
    @Schema(description = "The unique identifier of the role.", example = "e1f2g3h4-i5j6-7890-1234-567890abcdef")
    private UUID roleId;
    @NotNull
    @Schema(description = "The unique identifier of the action to be granted.", example = "d1e2f3g4-h5i6-7890-1234-567890abcdef")
    private UUID actionId;
    @NotNull
    @Schema(description = "The unique identifier of the enterprise.", example = "c1d2e3f4-g5h6-7890-1234-567890abcdef")
    private UUID enterpriseId;
}

