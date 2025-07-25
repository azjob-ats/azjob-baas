package com.app.boot_app.shared.infra.auth.firebase_sdk.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.app.boot_app.shared.exeception.model.BadRequestException;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.InternalServerErrorException;
import com.app.boot_app.shared.infra.auth.AuthAdapter;
import com.app.boot_app.shared.infra.auth.firebase_sdk.constant.Constant;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseRefresh;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseAuthServiceImpl implements AuthAdapter {

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    private final MessageSource messageSource;
    private final FirebaseAuth firebaseAuth;

    public void markEmailAsVerified(String email) {
        try {
            UserRecord userFirebase = FirebaseAuth.getInstance().getUserByEmail(email);
            UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(userFirebase.getUid())
                    .setEmailVerified(true);
            FirebaseAuth.getInstance().updateUser(updateRequest);

        } catch (FirebaseAuthException e) {
            throw new ConflictException("firebase/markEmailAsVerified",
                    "An unexpected error occurred while verifying the email.");

        }
    }

    public FirebaseToken getUserByToken(String token) {
        try {
            return firebaseAuth.verifyIdToken(token);

        } catch (FirebaseAuthException e) {
            throw new ConflictException("firebase/getUserByToken",
                    "Token must not be null or empty.");
        }
    }

    public void resetPassword(String email, String newPassword) {
        try {
            UserRecord userRecord = firebaseAuth.getUser(email);
            firebaseAuth.updateUser(new UserRecord.UpdateRequest(userRecord.getUid()).setPassword(newPassword));

        } catch (FirebaseAuthException e) {
            throw new ConflictException("firebase/resetPassword", "Reset token is missing or invalid.");
        }
    }

    public void isEmailVerified(String email) {
        try {

            UserRecord userRecord = firebaseAuth.getUserByEmail(email);

            if (!userRecord.isEmailVerified()) {
                throw new BadRequestException(
                        "firebase/isEmailVerified",
                        "email not verified firebase");
            }

        } catch (FirebaseAuthException e) {
            throw new ConflictException("firebase/isEmailVerified",
                    "Email is not verified. Please verify your email to proceed.");

        }
    }

    public UserRecord signUpWithEmailAndPassword(String email, String password, String firstName) {

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(firstName)
                .setEmailVerified(false)
                .setDisabled(false);

        try {
            return firebaseAuth.createUser(request);

        } catch (FirebaseAuthException e) {
            throw new InternalServerErrorException(
                    "firebase/signUpWithEmailAndPassword",
                    "An error occurred while creating the account. Please try again later.");
        }
    }

    @Override
    public FirebaseSignIn signInWithEmailAndPassword(String email, String password) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken",
                true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<FirebaseSignIn> response = restTemplate.exchange(
                    Constant.FIREBASE_URL + firebaseApiKey,
                    HttpMethod.POST,
                    entity,
                    FirebaseSignIn.class);

            return response.getBody();

        } catch (HttpClientErrorException e) {

            throw new ConflictException("firebase/signInWithEmailAndPassword",
                    "An unexpected error occurred during sign-in. Please try again later.");
        }
    }

    public FirebaseRefresh refreshIdToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=refresh_token&refresh_token=" + refreshToken;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<FirebaseRefresh> response = restTemplate.exchange(
                Constant.REFRESH_URL + firebaseApiKey,
                HttpMethod.POST,
                entity,
                FirebaseRefresh.class);

        return response.getBody();
    }

    public void revokeRefreshTokens(String uid) {
        try {
            FirebaseAuth.getInstance().revokeRefreshTokens(uid);
        } catch (FirebaseAuthException e) {
            throw new ConflictException("firebase/revokeRefreshTokens",
                    "An unexpected error occurred while revoking refresh tokens.");
        }
    }
}
