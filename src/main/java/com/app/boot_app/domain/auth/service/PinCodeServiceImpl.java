package com.app.boot_app.domain.auth.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.entity.PinCode;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.exception.ConflictException;
import com.app.boot_app.domain.auth.repository.PinCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class PinCodeServiceImpl implements PinCodeService {

    private final PinCodeRepository pinCodeRepository;
     private final MessageSource messageSource;

    public PinCodeServiceImpl(PinCodeRepository pinCodeRepository, MessageSource messageSource) {
        this.pinCodeRepository = pinCodeRepository;
        this.messageSource = messageSource;
    }

    @Override
    public String generateAndSavePinCode(User user) {
        String code = String.format("%06d", new Random().nextInt(999999));

        PinCode pinCode = new PinCode();
        pinCode.setUser(user);
        pinCode.setCode(code);
        pinCode.setEmail(user.getEmail());
        pinCode.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        pinCodeRepository.save(pinCode);

        return code;
    }

    @Override
    public boolean validatePinCode(UUID userId, String pin) {
    Optional<PinCode> optionalPin = pinCodeRepository.findByUserIdAndCode(userId, pin);

    if (optionalPin.isEmpty()) {
        // PIN não encontrado
        throw new ConflictException(
            "auth/wrong-pin-not-found",
            messageSource.getMessage("auth.pin.not-found", null, LocaleContextHolder.getLocale())
        );
    }

    PinCode pinCode = optionalPin.get();

    if (pinCode.getIsUsed()) {
        // PIN já usado
        throw new ConflictException(
            "auth/wrong-pin-used",
            messageSource.getMessage("auth.pin.used", null, LocaleContextHolder.getLocale())
        );
    }

    if (pinCode.getExpiresAt().isBefore(LocalDateTime.now())) {
        // PIN expirado
        throw new ConflictException(
            "auth/wrong-pin-expired",
            messageSource.getMessage("auth.pin.expired", null, LocaleContextHolder.getLocale())
        );
    }

    // Marca como usado e salva
    pinCode.setIsUsed(true);
    pinCodeRepository.save(pinCode);

    return true;
}

    
}
