package com.app.boot_app.shared.infra.auth.firebase_sdk.model;
import lombok.Data;

@Data
public class FirebaseSignIn {
    private String kind;
    private String localId;
    private String email;
    private String displayName;
    private String idToken;
    private boolean registered;
    private String refreshToken;
    private String expiresIn;
}