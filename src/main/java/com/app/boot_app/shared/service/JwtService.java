package com.app.boot_app.shared.service;

import java.util.UUID;

public interface JwtService {
    public String generateToken(String email);
    public String validateToken(String token);
}
