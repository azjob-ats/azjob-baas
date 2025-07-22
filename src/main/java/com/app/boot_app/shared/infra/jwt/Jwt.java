package com.app.boot_app.shared.infra.jwt;

import java.util.UUID;

public interface Jwt {
    public String extractEmail(String token);
    public String generateToken(String email);
    public String validateToken(String token);
}
