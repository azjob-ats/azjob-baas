package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO for sending a group invitation")
public class GroupInvitationRequestDTO {

    @NotBlank
    @Email
    @Schema(description = "Email of the user to invite", example = "invitee@example.com")
    private String email;

    @Schema(description = "ID of the role to suggest for the invited user", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID roleId;
}
