package com.app.boot_app.domain.auth.dto;
import java.util.UUID;

import lombok.Data;

@Data
public class FirebaseRefreshResponseDTO {
    private String accessToken;
    private String expiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;
    private UUID userId;
    private String projectId;
}
