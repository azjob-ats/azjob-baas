package com.app.boot_app.domain.auth.service;
import java.util.UUID;

import com.app.boot_app.domain.auth.dto.FirebaseRefreshResponseDTO;
import com.app.boot_app.domain.auth.dto.FirebaseSignInResponseDTO;

public interface FirebaseAuthService {
    public void revokeRefreshTokens(UUID uid);
    public FirebaseRefreshResponseDTO refreshIdToken(String refreshToken);
    public FirebaseSignInResponseDTO signInWithEmailAndPassword(String email, String password);
}
