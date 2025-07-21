package com.app.boot_app.domain.auth.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.boot_app.domain.auth.dto.AuthResponseDTO;
import com.app.boot_app.domain.auth.dto.ForgotPasswordRequestDTO;
import com.app.boot_app.domain.auth.dto.RefreshTokenRequestDTO;
import com.app.boot_app.domain.auth.dto.ResetPasswordRequestDTO;
import com.app.boot_app.domain.auth.dto.SendVerificationCodeRequestDTO;
import com.app.boot_app.domain.auth.dto.SignInRequestDTO;
import com.app.boot_app.domain.auth.dto.SignUpRequestDTO;
import com.app.boot_app.domain.auth.dto.VerifyAccountRequestDTO;
import com.app.boot_app.domain.auth.service.AuthService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    public AuthController(AuthService authService, MessageSource messageSource) {
        this.authService = authService;
        this.messageSource = messageSource;
    }

    @PostMapping("/sign-up-with-email-and-password")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        AuthResponseDTO result = authService.signUp(signUpRequestDTO);
        String message = messageSource.getMessage("auth.user.created", null, LocaleContextHolder.getLocale());
        return Response.ok(message, result);
    }

    @PostMapping("/sign-in-with-email-and-password")
    public ApiResponse<AuthResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        AuthResponseDTO result = authService.signIn(signInRequestDTO);
        return Response.ok(messageSource.getMessage("auth.user.loggedin", null, LocaleContextHolder.getLocale()),
                result);
    }

    @PostMapping("/verify-account")
    public ApiResponse<AuthResponseDTO> verifyAccount(
            @Valid @RequestBody VerifyAccountRequestDTO verifyAccountRequestDTO) {
        AuthResponseDTO result = authService.verifyAccount(verifyAccountRequestDTO);
        return Response.ok(
            messageSource.getMessage("auth.account.verified", null, LocaleContextHolder.getLocale()),
            result
        );
    }

    @PostMapping("/send-verification-code")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Boolean> sendVerificationCode(@Valid @RequestBody SendVerificationCodeRequestDTO request) {
        Boolean result = authService.sendVerificationCode(request.getEmail());
        return Response.ok(
                messageSource.getMessage("auth.verification.code.sent", null, LocaleContextHolder.getLocale()), result);
    }

    @PostMapping("/resend-verification-code")
    public ApiResponse<Boolean> resendVerificationCode(@Valid @RequestBody SendVerificationCodeRequestDTO request) {
        Boolean result = authService.resendVerificationCode(request.getEmail());
        return Response.ok(
            messageSource.getMessage("auth.verification.code.resent", null, LocaleContextHolder.getLocale()), 
            result
        );
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        AuthResponseDTO result = authService.refreshToken(request.getRefreshToken());
        return Response.ok(messageSource.getMessage("auth.token.refreshed", null, LocaleContextHolder.getLocale()),
                result);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        authService.forgotPassword(request.getEmail());
        return Response.ok(
                messageSource.getMessage("auth.password.reset.link.sent", null, LocaleContextHolder.getLocale()), null);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return Response.ok(messageSource.getMessage("auth.password.reset", null, LocaleContextHolder.getLocale()),
                null);
    }
}
