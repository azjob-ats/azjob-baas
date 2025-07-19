package com.app.boot_app.domain.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.dto.AuthResponseDTO;
import com.app.boot_app.domain.auth.dto.RefreshTokenDTO;
import com.app.boot_app.domain.auth.dto.SignInRequestDTO;
import com.app.boot_app.domain.auth.dto.SignUpRequestDTO;
import com.app.boot_app.domain.auth.dto.VerifyAccountRequestDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupAuditLog;
import com.app.boot_app.domain.auth.entity.GroupMember;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.enums.GroupName;
import com.app.boot_app.domain.auth.enums.ProviderName;
import com.app.boot_app.domain.auth.enums.RoleName;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import com.app.boot_app.domain.auth.exception.BadRequestException;
import com.app.boot_app.domain.auth.exception.ConflictException;
import com.app.boot_app.domain.auth.exception.InternalServerErrorException;
import com.app.boot_app.domain.auth.exception.NotFoundException;
import com.app.boot_app.domain.auth.repository.GroupMemberRepository;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.domain.auth.repository.PinCodeRepository;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.domain.auth.repository.UserRepository;
import com.app.boot_app.shared.service.EmailService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PinCodeRepository pinCodeRepository;
    private final FirebaseAuth firebaseAuth;
    private final EmailService emailService;
    private final PinCodeService pinCodeService;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupAuditLogService groupAuditLogService;
    private final MessageSource messageSource;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PinCodeRepository pinCodeRepository, FirebaseAuth firebaseAuth, EmailService emailService,
            PinCodeService pinCodeService, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository,
            GroupAuditLogService groupAuditLogService, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.pinCodeRepository = pinCodeRepository;
        this.firebaseAuth = firebaseAuth;
        this.emailService = emailService;
        this.pinCodeService = pinCodeService;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupAuditLogService = groupAuditLogService;
        this.messageSource = messageSource;
    }

    @Override
    public AuthResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        if (userRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
            throw new ConflictException("email-already-exists",
                    messageSource.getMessage("auth.email.exists", null, LocaleContextHolder.getLocale()));
        }

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(signUpRequestDTO.getEmail())
                .setPassword(signUpRequestDTO.getPassword())
                .setDisplayName(signUpRequestDTO.getFirstName() + " " + signUpRequestDTO.getLastName())
                .setEmailVerified(false)
                .setDisabled(false);

        try {
            UserRecord userRecord = firebaseAuth.createUser(request);

            User user = new User();
            user.setProvider(ProviderName.EMAIL_AND_PASSWORD_BY_GOOGLE.name());
            user.setEmail(signUpRequestDTO.getEmail());
            user.setPassword(signUpRequestDTO.getPassword());
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

            userRepository.save(user);

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

            // Log user creation
            GroupAuditLog auditLog = new GroupAuditLog();
            auditLog.setGroup(defaultGroup);
            auditLog.setAction(GroupAuditLog.Action.USER_CREATED.name());
            auditLog.setPerformedBy(user);
            auditLog.setPerformedAt(LocalDateTime.now());
            auditLog.setTargetUser(user);
            auditLog.setNewValue(JsonNodeFactory.instance.objectNode().put("email", user.getEmail()).toString()); // Convert
                                                                                                                  // ObjectNode
                                                                                                                  // to
                                                                                                                  // String
            groupAuditLogService.createAuditLog(auditLog);

            String pinCode = pinCodeService.generateAndSavePinCode(user);

            String emailText = messageSource.getMessage("auth.signup.email.body",
                    new Object[] { user.getFirstName(), pinCode }, LocaleContextHolder.getLocale());

            emailService.sendEmail(user.getEmail(), messageSource.getMessage("auth.signup.email.subject",
                    new Object[] { pinCode }, LocaleContextHolder.getLocale()), emailText);

            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());

            String accessToken = firebaseAuth.createCustomToken(userRecord.getUid(), claims);
           
            var refreshToken = RefreshTokenDTO.builder()
               .token(UUID.randomUUID().toString())
               .expiresIn(LocalDateTime.now().plusMinutes(30))
               .timestamp(LocalDateTime.now())
               .userId(user.getId())
               .build();

            return AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (FirebaseAuthException e) {
            throw new InternalServerErrorException("firebase-user-creation-error",
                    messageSource.getMessage("auth.firebase.user.creation.error", new Object[] { e.getMessage() },
                            LocaleContextHolder.getLocale()));
        }
    }

    private String createUsername(String email) {
        return email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
    }

    @Override
    public AuthResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        try {
    
            UserRecord userRecord = firebaseAuth.getUserByEmail(signInRequestDTO.getEmail());

            if (!userRecord.isEmailVerified()) {
                throw new BadRequestException(
                        "auth/email-not-verified-firebase",
                        messageSource.getMessage("auth.email.not.verified", null, LocaleContextHolder.getLocale()));
            }

            User user = userRepository.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("user-not-found",
                        messageSource.getMessage("auth.user.not.found", null, LocaleContextHolder.getLocale())));


            if (!user.getIsVerified()) {
                throw new BadRequestException(
                        "auth/email-not-verified",
                        messageSource.getMessage("auth.email.not.verified", null, LocaleContextHolder.getLocale()));
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("email", signInRequestDTO.getEmail());
            String accessToken = firebaseAuth.createCustomToken(userRecord.getUid(), claims);

            var refreshToken = RefreshTokenDTO.builder()
                    .token(UUID.randomUUID().toString())
                    .expiresIn(LocalDateTime.now().plusMinutes(30))
                    .timestamp(LocalDateTime.now())
                    .userId(user.getId())
                    .build();

            return AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (FirebaseAuthException e) {
            throw new ConflictException("invalid-credentials",
                    messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale()));

        }
    }
    
    @Override
    public AuthResponseDTO verifyAccount(VerifyAccountRequestDTO verifyAccountRequestDTO) {
        User user = userRepository.findByEmail(verifyAccountRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("auth/wrong-email",
                        messageSource.getMessage("auth.user.not.found", null, LocaleContextHolder.getLocale())));


        pinCodeService.validatePinCode(user.getId(), verifyAccountRequestDTO.getCode());

        // Mark email as verified in Firebase
        markEmailAsVerifiedInFirebase(user.getEmail());

        // Update user status in local database
        user.setIsVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        String accessToken;
        try {
            accessToken = firebaseAuth.createCustomToken(user.getId().toString(), claims);
        } catch (FirebaseAuthException e) {
            throw new InternalServerErrorException("firebase-token-creation-error",
                    messageSource.getMessage("auth.firebase.token.creation.error", new Object[]{e.getMessage()}, LocaleContextHolder.getLocale()));
        }

        var refreshToken = RefreshTokenDTO.builder()
                .token(UUID.randomUUID().toString())
                .expiresIn(LocalDateTime.now().plusMinutes(30))
                .timestamp(LocalDateTime.now())
                .userId(user.getId())
                .build();

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void sendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user-not-found",
                        messageSource.getMessage("auth.user.not.found", null, LocaleContextHolder.getLocale())));

        String pinCode = pinCodeService.generateAndSavePinCode(user);

        String emailText = messageSource.getMessage("auth.verification.code.email.body",
                new Object[] { user.getFirstName(), pinCode }, LocaleContextHolder.getLocale());

        emailService.sendEmail(user.getEmail(),
                messageSource.getMessage("auth.verification.code.email.subject", null, LocaleContextHolder.getLocale()),
                emailText);
    }

    @Override
    public void resendVerificationCode(String email) {
        // A lógica é a mesma que sendVerificationCode, invalidando o anterior (o que já
        // acontece ao gerar um novo)
        sendVerificationCode(email);
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        // In a real application, you would validate the refresh token against a
        // database
        // and then generate a new Firebase custom token.
        // For simplicity, we'll just generate a new custom token for a dummy user.
        try {
            // Assuming refreshToken contains the UID of the user for whom to generate a new
            // token
            // In a real scenario, you'd have a more robust refresh token management system
            String customToken = firebaseAuth.createCustomToken(refreshToken);
            return AuthResponseDTO.builder()
                    .accessToken(customToken)
                    .build();
        } catch (FirebaseAuthException e) {
            throw new ConflictException("invalid-refresh-token", messageSource.getMessage("auth.invalid.refresh.token",
                    new Object[] { e.getMessage() }, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user-not-found",
                        messageSource.getMessage("auth.user.not.found", null, LocaleContextHolder.getLocale())));

        // Generate a password reset link/token from Firebase
        try {
            String link = firebaseAuth.generatePasswordResetLink(email);
            // TODO: Send email with reset link
            String emailText = messageSource.getMessage("auth.forgot.password.email.body",
                    new Object[] { user.getFirstName(), link }, LocaleContextHolder.getLocale());
            emailService.sendEmail(user.getEmail(), messageSource.getMessage("auth.forgot.password.email.subject", null,
                    LocaleContextHolder.getLocale()), emailText);

        } catch (FirebaseAuthException e) {
            throw new InternalServerErrorException("password-reset-link-error",
                    messageSource.getMessage("auth.password.reset.link.error", new Object[] { e.getMessage() },
                            LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // In a real application, you would verify the token and then update the
        // password.
        // Firebase Admin SDK does not directly support verifying password reset tokens
        // generated by generatePasswordResetLink.
        // This token is meant to be used on the client-side with Firebase client SDK.
        // For this example, we'll assume the token is valid and update the user's
        // password directly in Firebase.
        try {
            // Assuming the token is actually the user's UID for simplicity in this example
            // In a real scenario, you'd have a custom token verification mechanism
            UserRecord userRecord = firebaseAuth.getUser(token);
            firebaseAuth.updateUser(new UserRecord.UpdateRequest(userRecord.getUid()).setPassword(newPassword));

            User user = userRepository.findByEmail(userRecord.getEmail())
                    .orElseThrow(() -> new NotFoundException("user-not-found",
                            messageSource.getMessage("auth.user.not.found", null, LocaleContextHolder.getLocale())));
            user.setPassword("********"); // Update local password representation if needed
            userRepository.save(user);

        } catch (FirebaseAuthException e) {
            throw new ConflictException("invalid-or-expired-token", messageSource.getMessage(
                    "auth.invalid.or.expired.token", new Object[] { e.getMessage() }, LocaleContextHolder.getLocale()));
        }
    }

    private void markEmailAsVerifiedInFirebase(String email){
        try {
                UserRecord userFirebase = FirebaseAuth.getInstance().getUserByEmail(email);
                UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(userFirebase.getUid())
                    .setEmailVerified(true);
                FirebaseAuth.getInstance().updateUser(updateRequest);

        } catch (FirebaseAuthException e) {
           throw new ConflictException("invalid-credentials",
                    messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale()));

        }
    }
}
