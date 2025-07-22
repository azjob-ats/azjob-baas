package com.app.boot_app.shared.infra.jwt.jwt_auth0;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.boot_app.shared.infra.jwt.Jwt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Service
public class JwtServiceImpl implements Jwt {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final int EXPIRATION_MINUTES = 10;

    public String generateToken(String email) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES)))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println(extractEmail(token));
            System.out.println(extractEmail(jwt.getSubject()));
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido ou expirado");
        }
    }

    public String extractEmail(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("claims").asMap().get("email").toString();
    }
}
