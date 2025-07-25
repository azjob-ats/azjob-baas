package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request DTO for validating a PIN for password update")
public class ValidatePinForUpdatePasswordRequestDTO {
    @NotBlank(message = "The user's email address is required.")
    @Email(message = "The email address must be valid.")

    @Schema(description = "The user's email address.", example = "user@example.com")
    private String email;
    
    @NotBlank(message = "The PIN code is required.")
    @Schema(description = "The PIN code sent to the user.", example = "123456")
    private String code;
}