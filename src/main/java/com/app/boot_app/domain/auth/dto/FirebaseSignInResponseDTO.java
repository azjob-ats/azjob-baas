package com.app.boot_app.domain.auth.dto;
import lombok.Data;

@Data
public class FirebaseSignInResponseDTO {
    private String kind;
    private String localId;
    private String email;
    private String displayName;
    private String idToken;
    private boolean registered;
    private String refreshToken;
    private String expiresIn;
}