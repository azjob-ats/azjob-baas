package com.app.boot_app.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserGroupResponseDTO {
    private UUID id;
    private UUID userId;
    private UUID groupId;
}
