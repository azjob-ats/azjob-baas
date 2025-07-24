package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request DTO for validating a PIN for password update")
public class ValidatePinForUpdatePasswordRequestDTO {
    @Schema(description = "The user's email address.", example = "user@example.com")
    private String email;
    @Schema(description = "The PIN code sent to the user.", example = "123456")
    private String code;
}
