package com.app.boot_app.shared.infra.email;

public interface Email {
    void sendEmail(String to, String subject, String text);
}
