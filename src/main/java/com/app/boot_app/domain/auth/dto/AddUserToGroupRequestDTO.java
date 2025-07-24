package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO for adding a user to a group")
public class AddUserToGroupRequestDTO {
    @NotNull
    @Schema(description = "The unique identifier of the user.", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID userId;
    @NotNull
    @Schema(description = "The unique identifier of the group.", example = "b1c2d3e4-f5g6-7890-1234-567890abcdef")
    private UUID groupId;
    @NotNull
    @Schema(description = "The unique identifier of the enterprise.", example = "c1d2e3f4-g5h6-7890-1234-567890abcdef")
    private UUID enterpriseId;
}
