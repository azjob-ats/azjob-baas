package com.app.boot_app.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EnterpriseResponseDTO {
    private UUID id;
    private String nameEnterprise;
    private UUID ownerId;
}
