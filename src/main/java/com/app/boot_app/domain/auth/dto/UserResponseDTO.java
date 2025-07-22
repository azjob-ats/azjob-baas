package com.app.boot_app.domain.auth.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response DTO for user information")
public class UserResponseDTO {

    @Schema(description = "Email of the user", example = "user@example.com")
    private String email;

    @Schema(description = "Username", example = "john_doe")
    private String username;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Phone number", example = "+55 11 91234-5678")
    private String phone;

    @Schema(description = "Birth date", example = "1990-01-01")
    private LocalDate birthDate;

    @Schema(description = "Gender", example = "male")
    private String gender;

    @Schema(description = "User avatar URL")
    private String avatar;

    @Schema(description = "Short biography")
    private String bio;

    @Schema(description = "Full address")
    private String address;

    @Schema(description = "City", example = "São Paulo")
    private String city;

    @Schema(description = "State", example = "SP")
    private String state;

    @Schema(description = "Country", example = "Brazil")
    private String country;

    @Schema(description = "Zip Code", example = "01000-000")
    private String zipCode;

    @Schema(description = "Role assigned to the user")
    private UUID roleId;

    @Schema(description = "Time zone", example = "America/Sao_Paulo")
    private String timeZone;

    @Schema(description = "Date of account creation")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Indicates if user is active")
    private Boolean isActive;

    @Schema(description = "Indicates if user has verified the account")
    private Boolean isVerified;

    @Schema(description = "Indicates if the user is blocked")
    private Boolean isBlocked;
}
