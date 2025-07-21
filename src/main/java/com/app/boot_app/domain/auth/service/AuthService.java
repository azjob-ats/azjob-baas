package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.AuthResponseDTO;
import com.app.boot_app.domain.auth.dto.SignInRequestDTO;
import com.app.boot_app.domain.auth.dto.SignUpRequestDTO;
import com.app.boot_app.domain.auth.dto.UserResponseDTO;
import com.app.boot_app.domain.auth.dto.VerifyAccountRequestDTO;

public interface AuthService {
    AuthResponseDTO signUp(SignUpRequestDTO signUpRequestDTO);
    AuthResponseDTO signIn(SignInRequestDTO signInRequestDTO);
    AuthResponseDTO verifyAccount(VerifyAccountRequestDTO verifyAccountRequestDTO);
    Boolean sendVerificationCode(String email);
    boolean resendVerificationCode(String email);
    AuthResponseDTO refreshToken(String refreshToken);
    Boolean forgotPassword(String email);
    Boolean resetPassword(String token, String newPassword);
    UserResponseDTO getUserByToken(String token);
}
