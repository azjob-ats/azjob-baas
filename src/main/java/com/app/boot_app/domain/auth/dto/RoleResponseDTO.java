package com.app.boot_app.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoleResponseDTO {
    private UUID id;
    private String name;
    private String description;
}
