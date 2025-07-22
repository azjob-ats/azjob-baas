package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.constant.Constant;
import com.app.boot_app.domain.auth.dto.FirebaseRefreshResponseDTO;
import com.app.boot_app.domain.auth.dto.FirebaseSignInResponseDTO;
import com.app.boot_app.domain.auth.exception.ConflictException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseAuthServiceImpl implements FirebaseAuthService {

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    private final MessageSource messageSource;

    @Override
    public FirebaseSignInResponseDTO signInWithEmailAndPassword(String email, String password) {
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
            ResponseEntity<FirebaseSignInResponseDTO> response = restTemplate.exchange(
                    Constant.FIREBASE_URL + firebaseApiKey,
                    HttpMethod.POST,
                    entity,
                    FirebaseSignInResponseDTO.class);

            return response.getBody();

        } catch (HttpClientErrorException e) {

            throw new ConflictException("auth/email-password-invalid",
                    messageSource.getMessage("auth.email.or.password.invalid", null, LocaleContextHolder.getLocale()));
        }
    }

    public FirebaseRefreshResponseDTO refreshIdToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=refresh_token&refresh_token=" + refreshToken;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<FirebaseRefreshResponseDTO> response = restTemplate.exchange(
                Constant.REFRESH_URL + firebaseApiKey,
                HttpMethod.POST,
                entity,
                FirebaseRefreshResponseDTO.class);

        return response.getBody();
    }

    public void revokeRefreshTokens(UUID uid) {
        try {
            FirebaseAuth.getInstance().revokeRefreshTokens(uid.toString());
        } catch (FirebaseAuthException e) {
            throw new ConflictException("auth/revoke-refresh-token-invalid",
                messageSource.getMessage("auth.revoke-refresh-token-invalid", null, LocaleContextHolder.getLocale()));
        }
    }
}
