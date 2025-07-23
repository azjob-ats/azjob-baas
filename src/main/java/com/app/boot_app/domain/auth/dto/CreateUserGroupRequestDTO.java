package com.app.boot_app.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserGroupRequestDTO {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID groupId;
}
