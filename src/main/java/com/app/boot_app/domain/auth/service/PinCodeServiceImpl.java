package com.app.boot_app.domain.auth.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.entity.PinCode;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.exception.ConflictException;
import com.app.boot_app.domain.auth.repository.PinCodeRepository;

import java.time.LocalDateTime;
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
        PinCode pinCode = pinCodeRepository.findByUserIdAndCode(userId, pin)
            .filter(code -> !code.getIsUsed())
            .filter(code -> code.getExpiresAt().isAfter(LocalDateTime.now()))
            .orElseThrow(() -> new ConflictException(
                "auth/wrong-pin-invalid",
                messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale())
            ));

        pinCode.setIsUsed(true);
        pinCodeRepository.save(pinCode);

        return true;
    }
    
}
