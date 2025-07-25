package com.app.boot_app.core.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.boot_app.shared.exeception.model.ConflictException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirebaseRequest extends OncePerRequestFilter {

    private final FirebaseAuth firebaseAuth;

    public FirebaseRequest(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseToken getFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String idToken = authorizationHeader.substring(7);
        return validateToken(idToken);
    }

    public FirebaseToken validateToken(String idToken){
        try {
            return  firebaseAuth.verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            throw new ConflictException("firebase/verifyIdToken",
                    "An unexpected error occurred while verify id token the token.");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = authorizationHeader.substring(7);

        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            filterChain.doFilter(request, response);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            String json = String.format("""
                    {
                        "success": false,
                        "statusCode": 401,
                        "message": "Unauthorized: %s",
                        "error": {
                            "code": "auth/token-invalid",
                            "description": "%s"
                        },
                        "timestamp": "%s"
                    }
                    """, e.getAuthErrorCode().name(), e.getMessage(), java.time.ZonedDateTime.now());

            response.getWriter().write(json);
            return;
        }
    }
}
