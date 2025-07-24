package com.app.boot_app.shared.infra.jwt;


public interface Jwt {
    public String extractEmail(String token);

    public String generateToken(String email);

    public String validateToken(String token);
}
