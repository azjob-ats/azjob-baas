package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Response DTO for group invitation details")
public class GroupInvitationResponseDTO {
    @Schema(description = "Unique identifier of the invitation", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "ID of the group the invitation is for", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID groupId;
    @Schema(description = "Email of the invited user", example = "invitee@example.com")
    private String email;
    @Schema(description = "Unique token for the invitation", example = "some-unique-token")
    private String token;
    @Schema(description = "ID of the suggested role for the invited user", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID roleId;
    @Schema(description = "ID of the user who sent the invitation", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID invitedBy;
    @Schema(description = "Timestamp when the invitation was created", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp when the invitation expires", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime expiresAt;
    @Schema(description = "Current status of the invitation (PENDING, ACCEPTED, EXPIRED, CANCELLED)", example = "PENDING")
    private String status;
}
