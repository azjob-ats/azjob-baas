package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO for adding or updating a group member")
public class GroupMemberRequestDTO {

    @NotNull
    @Schema(description = "ID of the user to be added or updated", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID userId;

    @Schema(description = "ID of the role assigned to the member within the group", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID assignedRoleId;
}
