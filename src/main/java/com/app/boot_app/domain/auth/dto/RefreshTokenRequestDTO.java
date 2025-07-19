package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request DTO for refreshing access token")
public class RefreshTokenRequestDTO {

    @NotBlank
    @Schema(description = "The refresh token string", example = "some-long-refresh-token-string")
    private String refreshToken;
}
