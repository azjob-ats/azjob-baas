package com.app.boot_app.shared.infra.auth.firebase_sdk.service;

import com.app.boot_app.domain.auth.dto.AuthResponseDTO;
import com.app.boot_app.domain.auth.dto.RefreshTokenDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupAuditLog;
import com.app.boot_app.domain.auth.entity.GroupMember;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.enums.GroupName;
import com.app.boot_app.domain.auth.enums.ProviderName;
import com.app.boot_app.domain.auth.enums.RoleName;
import com.app.boot_app.shared.exeception.model.BadRequestException;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.InternalServerErrorException;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import com.app.boot_app.shared.infra.auth.AuthAdapter;
import com.app.boot_app.shared.infra.auth.firebase_sdk.constant.Constant;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseRefresh;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseSignIn;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
            throw new ConflictException("invalid-credentials",
                    messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale()));

        }
    }

    public FirebaseToken getUserByToken(String token) {
        try {
            return firebaseAuth.verifyIdToken(token);

        } catch (FirebaseAuthException e) {
            throw new ConflictException("invalid-credentials",
                    messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale()));
        }
    }

    public void resetPassword(String email, String newPassword) {
        try {
            UserRecord userRecord = firebaseAuth.getUser(email);
            firebaseAuth.updateUser(new UserRecord.UpdateRequest(userRecord.getUid()).setPassword(newPassword));

        } catch (FirebaseAuthException e) {
            throw new ConflictException("invalid-or-expired-token", messageSource.getMessage(
                    "auth.invalid.or.expired.token", new Object[] { e.getMessage() }, LocaleContextHolder.getLocale()));
        }
    }

    public void isEmailVerified(String email) {
        try {

            UserRecord userRecord = firebaseAuth.getUserByEmail(email);

            if (!userRecord.isEmailVerified()) {
                throw new BadRequestException(
                        "auth/email-not-verified-firebase",
                        messageSource.getMessage("auth.email.not.verified", null, LocaleContextHolder.getLocale()));
            }

        } catch (FirebaseAuthException e) {
            throw new ConflictException("invalid-credentials",
                    messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale()));

        }
    }

    public UserRecord signUpWithEmailAndPassword(String email, String password, String firstName) {

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(firstName)
                .setEmailVerified(false)
                .setDisabled(false);

        System.out.println(request.toString());
        try {
            return firebaseAuth.createUser(request);

        } catch (FirebaseAuthException e) {
            throw new InternalServerErrorException("firebase-user-creation-error",
                    messageSource.getMessage("auth.firebase.user.creation.error", new Object[] { e.getMessage() },
                            LocaleContextHolder.getLocale()));
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

            throw new ConflictException("auth/email-password-invalid",
                    messageSource.getMessage("auth.email.or.password.invalid", null, LocaleContextHolder.getLocale()));
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

    public void revokeRefreshTokens(UUID uid) {
        try {
            FirebaseAuth.getInstance().revokeRefreshTokens(uid.toString());
        } catch (FirebaseAuthException e) {
            throw new ConflictException("auth/revoke-refresh-token-invalid",
                    messageSource.getMessage("auth.revoke-refresh-token-invalid", null,
                            LocaleContextHolder.getLocale()));
        }
    }
}
