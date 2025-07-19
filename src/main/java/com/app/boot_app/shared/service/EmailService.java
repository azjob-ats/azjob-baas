package com.app.boot_app.shared.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
