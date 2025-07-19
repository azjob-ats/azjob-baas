package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Response DTO for group member details")
public class GroupMemberResponseDTO {
    @Schema(description = "Unique identifier of the group member association", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "ID of the group the member belongs to", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID groupId;
    @Schema(description = "ID of the user who is a member", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID userId;
    @Schema(description = "ID of the role assigned to the member within the group", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID assignedRoleId;
    @Schema(description = "Timestamp when the user joined the group", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime joinedAt;
    @Schema(description = "ID of the user who invited this member", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID invitedBy;
    @Schema(description = "Timestamp when the invitation to join the group expires", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime invitationExpiresAt;
    @Schema(description = "Whether the group membership is active", example = "true")
    private Boolean isActive;
}
