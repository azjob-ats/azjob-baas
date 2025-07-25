package com.app.boot_app.domain.auth.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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

    @Schema(description = "URL of the user's avatar.", example = "https://example.com/avatar.png")
    private String avatar;

    @Schema(description = "A short biography of the user.", example = "Software engineer specializing in backend development.")
    private String bio;

    @Schema(description = "The user's full address.", example = "123 Main Street, Apt 4B")
    private String address;

    @Schema(description = "The city where the user resides.", example = "New York")
    private String city;

    @Schema(description = "The state or province where the user resides.", example = "NY")
    private String state;

    @Schema(description = "The country where the user resides.", example = "USA")
    private String country;

    @Schema(description = "The postal code for the user's address.", example = "10001")
    private String zipCode;

    @Schema(description = "The user's time zone.", example = "America/New_York")
    private String timeZone;

    @Schema(description = "Timestamp indicating when the user's account was created.", example = "2023-01-15T10:30:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp indicating the last time the user's account was updated.", example = "2023-10-28T15:45:10Z")
    private LocalDateTime updatedAt;

    @Schema(description = "Indicates whether the user's account is active.", example = "true")
    private Boolean isActive;

    @Schema(description = "Indicates whether the user's account has been verified.", example = "true")
    private Boolean isVerified;

    @Schema(description = "Indicates whether the user's account is blocked.", example = "false")
    private Boolean isBlocked;

    @Schema(description = "The provider of the user's identity (e.g., google, facebook).", example = "google")
    private String idProvider;

    @Schema(description = "Unique identifier of the user", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;
}
