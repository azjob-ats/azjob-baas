package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request DTO for user sign-up")
public class SignUpRequestDTO {

    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "newuser@example.com")
    private String email;

    @NotBlank
    @Size(min = 5)
    @Schema(description = "User's password (min 8 characters)", example = "Senha!")
    private String password;

    @NotBlank
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;
}