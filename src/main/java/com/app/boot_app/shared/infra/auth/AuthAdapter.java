package com.app.boot_app.shared.infra.auth;

import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseRefresh;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseSignIn;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

public interface AuthAdapter {

    boolean checkEmailExists(String email);

    void markEmailAsVerified(String email);

    FirebaseToken getUserByToken(String token);

    void resetPassword(String email, String newPassword);

    void isEmailVerified(String email);

    UserRecord signUpWithEmailAndPassword(String email, String password, String firstName);

    void revokeRefreshTokens(String uid);

    FirebaseRefresh refreshIdToken(String refreshToken);

    FirebaseSignIn signInWithEmailAndPassword(String email, String password);
}
