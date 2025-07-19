package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Details of the refresh token")
public class RefreshTokenDTO {
    @Schema(description = "The refresh token string", example = "some-long-refresh-token-string")
    private String token;
    @Schema(description = "Timestamp when the refresh token was issued", example = "2025-07-17T23:00:00.000Z")
    private LocalDateTime timestamp;
    @Schema(description = "Timestamp when the refresh token expires", example = "2025-07-17T23:30:00.000Z")
    private LocalDateTime expiresIn;
    @Schema(description = "ID of the user associated with the refresh token", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID userId;
}
