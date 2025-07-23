package com.app.boot_app.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RecruiterResponseDTO {
    private UUID id;
    private String nameRecruiter;
    private UUID userId;
}
