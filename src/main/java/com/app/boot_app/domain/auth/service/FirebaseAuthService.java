package com.app.boot_app.domain.auth.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FirebaseAuthService {

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    private static final String FIREBASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";

    public String login(String email, String password) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", true
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    FIREBASE_URL + firebaseApiKey, HttpMethod.POST, entity, Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("idToken")) {
                return (String) responseBody.get("idToken");
            }

            throw new RuntimeException("Login inválido");

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("E-mail ou senha incorretos");
        }
    }
}
