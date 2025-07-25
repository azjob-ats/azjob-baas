package com.app.boot_app.domain.auth.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.constant.Constant;
import com.app.boot_app.domain.auth.dto.AuthResponseDTO;
import com.app.boot_app.domain.auth.dto.RefreshTokenDTO;
import com.app.boot_app.domain.auth.dto.SignInRequestDTO;
import com.app.boot_app.domain.auth.dto.SignUpRequestDTO;
import com.app.boot_app.domain.auth.dto.UserResponseDTO;
import com.app.boot_app.domain.auth.dto.VerifyAccountRequestDTO;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.mapper.UserMapper;
import com.app.boot_app.domain.auth.repository.UserRepository;
import com.app.boot_app.shared.exeception.model.BadRequestException;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import com.app.boot_app.shared.infra.auth.AuthAdapter;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseRefresh;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseSignIn;
import com.app.boot_app.shared.infra.email.Email;
import com.app.boot_app.shared.infra.jwt.Jwt;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UserRepository userRepository;
        private final Email emailService;
        private final PinCodeService pinCodeService;
        private final UserMapper userMapper;
        private final AuthAdapter authAdapter;
        private final Jwt jwtService;

        @Override
        public Boolean signUp(SignUpRequestDTO signUpRequestDTO) {
                if (userRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
                        throw new ConflictException("Auth/signUp", "Email already exists");
                }

                var userRecord = authAdapter.signUpWithEmailAndPassword(
                                signUpRequestDTO.getEmail(),
                                signUpRequestDTO.getPassword(),
                                signUpRequestDTO.getFirstName() + " " + signUpRequestDTO.getLastName());

                User user = new User();
                user.setProvider(Constant.EMAIL_AND_PASSWORD_BY_GOOGLE);
                user.setEmail(signUpRequestDTO.getEmail());
                user.setPassword("******");
                user.setIdProvider(userRecord.getUid());
                user.setFirstName(signUpRequestDTO.getFirstName());
                user.setLastName(signUpRequestDTO.getLastName());
                user.setUsername(createUsername(signUpRequestDTO.getEmail()));

                User registeredUser = userRepository.save(user);
                sendVerificationCode(registeredUser.getEmail());
                return true;

        }

        private String createUsername(String email) {
                return email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
        }

        @Override
        public AuthResponseDTO signIn(SignInRequestDTO signInRequestDTO) {

                authAdapter.isEmailVerified(signInRequestDTO.getEmail());

                User user = userRepository.findByEmail(signInRequestDTO.getEmail())
                                .orElseThrow(() -> new NotFoundException("Auth/signIn", "User not found"));

                if (!user.getIsVerified()) {
                        throw new BadRequestException(
                                        "Auth/signIn",
                                        "Email not verified");
                }

                FirebaseSignIn res = authAdapter.signInWithEmailAndPassword(
                                signInRequestDTO.getEmail(),
                                signInRequestDTO.getPassword());

                var refreshToken = RefreshTokenDTO.builder()
                                .token(res.getRefreshToken())
                                .expiresIn(LocalDateTime.now().plusMinutes(60))
                                .timestamp(LocalDateTime.now())
                                .userId(user.getId())
                                .build();

                return AuthResponseDTO.builder()
                                .accessToken(res.getIdToken())
                                .refreshToken(refreshToken)
                                .build();

        }

        @Override
        public Boolean verifyAccount(VerifyAccountRequestDTO verifyAccountRequestDTO) {
                User user = userRepository.findByEmail(verifyAccountRequestDTO.getEmail())
                                .orElseThrow(() -> new NotFoundException("Auth/verifyAccount", "User not found"));

                pinCodeService.validatePinCode(
                                user.getId(),
                                verifyAccountRequestDTO.getCode());

                authAdapter.markEmailAsVerified(user.getEmail());

                user.setIsVerified(true);
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);

                return true;
        }

        @Override
        public Boolean sendVerificationCode(String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("Auth/sendVerificationCode",
                                                "User not found"));

                String pinCode = pinCodeService.generateAndSavePinCode(user);

                String emailText = String.format("Hi %s, your verification code is %s", user.getFirstName(), pinCode);

                emailService.sendEmail(user.getEmail(), "Verification Code", emailText);

                return true;
        }

        @Override
        public Boolean resendVerificationCode(String email) {
                sendVerificationCode(email);

                return true;
        }

        @Override
        public Boolean forgotPassword(String email) {
                userRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("Auth/forgotPassword", "User not found"));

                sendVerificationCode(email);
                return true;
        }

        public String validatePinForUpdatePassword(String code, String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("Auth/validatePinForUpdatePassword",
                                                "User not found"));

                pinCodeService.validatePinCode(
                                user.getId(),
                                code);

                return jwtService.generateToken(user.getEmail());
        }

        @Override
        public Boolean resetPassword(String token, String newPassword) {
                String email = jwtService.validateToken(token);

                authAdapter.resetPassword(email, newPassword);

                return true;
        }

        @Override
        public AuthResponseDTO refreshToken(String refreshToken) {
                try {
                        FirebaseRefresh token = authAdapter.refreshIdToken(refreshToken);

                        var refresh = RefreshTokenDTO.builder()
                                        .token(token.getRefreshToken())
                                        .expiresIn(LocalDateTime.now().plusMinutes(60))
                                        .timestamp(LocalDateTime.now())
                                        .userId(token.getUserId())
                                        .build();

                        return AuthResponseDTO.builder()
                                        .accessToken(token.getIdToken())
                                        .refreshToken(refresh)
                                        .build();

                } catch (Exception e) {
                        throw new ConflictException("Auth/refreshToken", "Invalid refresh token: " + e.getMessage());
                }
        }

        @Override
        public UserResponseDTO getUserByToken(String token) {
                var decodedToken = authAdapter.getUserByToken(token);
                User user = userRepository.findByEmail(decodedToken.getEmail())
                                .orElseThrow(() -> new NotFoundException("Auth/getUserByToken", "User not found"));
                return userMapper.toResponse(user);

        }

        public UserResponseDTO getUserByEmail(String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("Auth/getUserByEmail", "User not found"));
                return userMapper.toResponse(user);
        }
}
