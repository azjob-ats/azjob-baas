package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Response DTO for group audit log details")
public class GroupAuditLogResponseDTO {
    @Schema(description = "Unique identifier of the audit log entry", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "ID of the group that was audited", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID groupId;
    @Schema(description = "Type of action performed (e.g., ADD_MEMBER, REMOVE_MEMBER, UPDATE_GROUP)", example = "ADD_MEMBER")
    private String action;
    @Schema(description = "ID of the user who performed the action", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID performedBy;
    @Schema(description = "Timestamp when the action was performed", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime performedAt;
    @Schema(description = "ID of the user affected by the action (if applicable)", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID targetUserId;
    @Schema(description = "JSON string of the old value before the change (if applicable)", example = "{\"name\": \"Old Name\"}")
    private String oldValue;
    @Schema(description = "JSON string of the new value after the change (if applicable)", example = "{\"name\": \"New Name\"}")
    private String newValue;
    @Schema(description = "IP address from which the action was performed", example = "192.168.1.1")
    private String ipAddress;
}
