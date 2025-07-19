package com.app.boot_app.domain.auth.service;

import java.util.UUID;

import com.app.boot_app.domain.auth.entity.User;

public interface PinCodeService {
    String generateAndSavePinCode(User user);
    boolean validatePinCode(UUID id, String pin);
}
