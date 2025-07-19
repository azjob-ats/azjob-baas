package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request DTO for account verification")
public class VerifyAccountRequestDTO {

    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Verification code sent to email", example = "123456")
    private String code;
}
