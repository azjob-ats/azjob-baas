package com.app.boot_app.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PermissionResponseDTO {
    private UUID id;
    private UUID idRole;
    private UUID idAction;
    private Boolean allowed;
    private UUID idEnterprise;
    private UUID idGroup;
}
