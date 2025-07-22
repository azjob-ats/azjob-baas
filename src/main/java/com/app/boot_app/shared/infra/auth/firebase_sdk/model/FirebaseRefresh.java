package com.app.boot_app.shared.infra.auth.firebase_sdk.model;
import java.util.UUID;

import lombok.Data;

@Data
public class FirebaseRefresh {
    private String accessToken;
    private String expiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;
    private UUID userId;
    private String projectId;
}
