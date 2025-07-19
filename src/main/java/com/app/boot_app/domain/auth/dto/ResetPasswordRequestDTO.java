package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request DTO for resetting password")
public class ResetPasswordRequestDTO {

    @NotBlank
    @Schema(description = "Token received for password reset", example = "some-reset-token")
    private String token;

    @NotBlank
    @Size(min = 8)
    @Schema(description = "New password (min 8 characters)", example = "newsecurepassword")
    private String newPassword;
}
