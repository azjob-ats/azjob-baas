package com.app.boot_app.domain.auth.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.dto.AuthResponseDTO;
import com.app.boot_app.domain.auth.dto.RefreshTokenDTO;
import com.app.boot_app.domain.auth.dto.SignInRequestDTO;
import com.app.boot_app.domain.auth.dto.SignUpRequestDTO;
import com.app.boot_app.domain.auth.dto.UserResponseDTO;
import com.app.boot_app.domain.auth.dto.VerifyAccountRequestDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupAuditLog;
import com.app.boot_app.domain.auth.entity.GroupMember;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.enums.GroupName;
import com.app.boot_app.domain.auth.enums.ProviderName;
import com.app.boot_app.domain.auth.enums.RoleName;
import com.app.boot_app.domain.auth.mapper.UserMapper;
import com.app.boot_app.domain.auth.repository.GroupMemberRepository;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.domain.auth.repository.UserRepository;
import com.app.boot_app.shared.exeception.model.BadRequestException;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import com.app.boot_app.shared.infra.auth.AuthAdapter;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseRefresh;
import com.app.boot_app.shared.infra.auth.firebase_sdk.model.FirebaseSignIn;
import com.app.boot_app.shared.infra.email.Email;
import com.app.boot_app.shared.infra.jwt.Jwt;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final Email emailService;
        private final PinCodeService pinCodeService;
        private final GroupRepository groupRepository;
        private final GroupMemberRepository groupMemberRepository;
        private final GroupAuditLogService groupAuditLogService;
        private final MessageSource messageSource;
        private final UserMapper userMapper;
        private final AuthAdapter authAdapter;
        private final Jwt jwtService;

        @Override
        public Boolean signUp(SignUpRequestDTO signUpRequestDTO) {
                if (userRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
                        throw new ConflictException("email-already-exists",
                                        messageSource.getMessage("auth.email.exists", null,
                                                        LocaleContextHolder.getLocale()));
                }

                var userRecord = authAdapter.signUpWithEmailAndPassword(
                                signUpRequestDTO.getEmail(),
                                signUpRequestDTO.getPassword(),
                                signUpRequestDTO.getFirstName() + " " + signUpRequestDTO.getLastName());

                User user = new User();
                user.setProvider(ProviderName.EMAIL_AND_PASSWORD_BY_GOOGLE.name());
                user.setEmail(signUpRequestDTO.getEmail());
                user.setPassword("******");
                user.setIdProvider(userRecord.getUid());
                user.setFirstName(signUpRequestDTO.getFirstName());
                user.setLastName(signUpRequestDTO.getLastName());
                user.setUsername(createUsername(signUpRequestDTO.getEmail()));

                Role defaultRole = roleRepository.findByName(RoleName.USER.getName())
                                .orElseGet(() -> {
                                        Role newRole = new Role();
                                        newRole.setName(RoleName.USER.getName());
                                        newRole.setDescription(RoleName.USER.getDescription());
                                        newRole.setIsDefault(true);
                                        newRole.setLevel(RoleName.USER.getLevel());
                                        newRole.setCreatedAt(LocalDateTime.now());
                                        newRole.setUpdatedAt(LocalDateTime.now());
                                        return roleRepository.save(newRole);
                                });
                user.setRole(defaultRole);

                User registeredUser = userRepository.save(user);

                Group defaultGroup = groupRepository.findByName(GroupName.DEFAULT_GROUP.getName())
                                .orElseGet(() -> {
                                        Group newGroup = new Group();
                                        newGroup.setName(GroupName.DEFAULT_GROUP.getName());
                                        newGroup.setDescription(GroupName.DEFAULT_GROUP.getDescription());
                                        newGroup.setIsActive(true);
                                        newGroup.setCreatedAt(LocalDateTime.now());
                                        newGroup.setUpdatedAt(LocalDateTime.now());
                                        newGroup.setCreatedBy(user); // Now user has an ID
                                        return groupRepository.save(newGroup);
                                });

                GroupMember groupMember = new GroupMember();
                groupMember.setGroup(defaultGroup);
                groupMember.setUser(user);
                groupMember.setAssignedRole(defaultRole);
                groupMember.setJoinedAt(LocalDateTime.now());
                groupMember.setIsActive(true);
                groupMemberRepository.save(groupMember);

                GroupAuditLog auditLog = new GroupAuditLog();
                auditLog.setGroup(defaultGroup);
                auditLog.setAction(GroupAuditLog.Action.USER_CREATED.name());
                auditLog.setPerformedBy(user);
                auditLog.setPerformedAt(LocalDateTime.now());
                auditLog.setTargetUser(user);
                auditLog.setNewValue(JsonNodeFactory.instance.objectNode().put("email", user.getEmail()).toString());
                groupAuditLogService.createAuditLog(auditLog);
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
                                .orElseThrow(() -> new NotFoundException("user-not-found",
                                                messageSource.getMessage("auth.user.not.found", null,
                                                                LocaleContextHolder.getLocale())));

                if (!user.getIsVerified()) {
                        throw new BadRequestException(
                                        "auth/email-not-verified",
                                        messageSource.getMessage("auth.email.not.verified", null,
                                                        LocaleContextHolder.getLocale()));
                }

                Map<String, Object> claims = new HashMap<>();
                claims.put("email", signInRequestDTO.getEmail());
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
                                .orElseThrow(() -> new NotFoundException("auth/wrong-email",
                                                messageSource.getMessage("auth.user.not.found", null,
                                                                LocaleContextHolder.getLocale())));

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
                                .orElseThrow(() -> new NotFoundException("user-not-found",
                                                messageSource.getMessage("auth.user.not.found", null,
                                                                LocaleContextHolder.getLocale())));

                String pinCode = pinCodeService.generateAndSavePinCode(user);

                String emailText = messageSource.getMessage("auth.verification.code.email.body",
                                new Object[] { user.getFirstName(), pinCode }, LocaleContextHolder.getLocale());

                emailService.sendEmail(user.getEmail(),
                                messageSource.getMessage("auth.verification.code.email.subject", null,
                                                LocaleContextHolder.getLocale()),
                                emailText);

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
                                .orElseThrow(() -> new NotFoundException("user-not-found",
                                                messageSource.getMessage("auth.user.not.found", null,
                                                                LocaleContextHolder.getLocale())));

                sendVerificationCode(email);
                return true;
        }

        public String validatePinForUpdatePassword(String code, String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("auth/wrong-email",
                                                messageSource.getMessage("auth.user.not.found", null,
                                                                LocaleContextHolder.getLocale())));

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
                        throw new ConflictException("invalid-refresh-token",
                                        messageSource.getMessage("auth.invalid.refresh.token",
                                                        new Object[] { e.getMessage() },
                                                        LocaleContextHolder.getLocale()));
                }
        }

        @Override
        public UserResponseDTO getUserByToken(String token) {
                var decodedToken = authAdapter.getUserByToken(token);
                User user = userRepository.findByEmail(decodedToken.getEmail())
                                .orElseThrow(() -> new NotFoundException("user-not-found",
                                                messageSource.getMessage("auth.user.not.found", null,
                                                                LocaleContextHolder.getLocale())));
                return userMapper.toResponse(user);

        }
}
