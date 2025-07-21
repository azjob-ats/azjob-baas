package com.app.boot_app.shared.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtExtract {

    public static String extractEmail(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("claims").asMap().get("email").toString();
    }
}