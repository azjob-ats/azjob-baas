package com.app.boot_app.domain.auth.service;

import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.entity.PinCode;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.repository.PinCodeRepository;
import com.app.boot_app.shared.exeception.model.ConflictException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class PinCodeServiceImpl implements PinCodeService {

    private final PinCodeRepository pinCodeRepository;

    public PinCodeServiceImpl(PinCodeRepository pinCodeRepository) {
        this.pinCodeRepository = pinCodeRepository;
    }

    @Override
    public String generateAndSavePinCode(User user) {
        String code = String.format("%06d", new Random().nextInt(999999));

        PinCode pinCode = new PinCode();
        pinCode.setUser(user);
        pinCode.setCode(code);
        pinCode.setEmail(user.getEmail());
        pinCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        pinCodeRepository.save(pinCode);

        return code;
    }

    @Override
    public boolean validatePinCode(UUID userId, String pin) {
        Optional<PinCode> optionalPin = pinCodeRepository.findByUserIdAndCode(userId, pin);

        if (optionalPin.isEmpty()) {
            // PIN não encontrado
            throw new ConflictException(
                    "PinCode/validatePinCode/isEmpty",
                    "Pin not found");
        }

        PinCode pinCode = optionalPin.get();

        if (pinCode.getIsUsed()) {
            // PIN já usado
            throw new ConflictException(
                    "PinCode/validatePinCode/getIsUsed",
                    "Pin already used");
        }

        if (pinCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            // PIN expirado
            throw new ConflictException(
                    "PinCode/validatePinCode/getExpiresAt",
                    "Pin expired");
        }

        // Marca como usado e salva
        pinCode.setIsUsed(true);
        pinCodeRepository.save(pinCode);

        return true;
    }

}
