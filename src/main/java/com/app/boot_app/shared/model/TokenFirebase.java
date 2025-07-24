package com.app.boot_app.shared.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class TokenFirebase {
    private String name;
    private String iss;
    private String aud;
    private Long auth_time;
    private String user_id;
    private String sub;
    private Long iat;
    private Long exp;
    private String email;
    private Boolean email_verified;
    private Firebase firebase;

    @Data
    @Builder
    public static class Firebase {
        private Map<String, List<String>> identities;
        private String sign_in_provider;
    }
}
