package com.app.boot_app.domain.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.boot_app.domain.auth.dto.GroupInvitationRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupInvitationResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupInvitation;
import com.app.boot_app.domain.auth.entity.GroupMember;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.repository.GroupInvitationRepository;
import com.app.boot_app.domain.auth.repository.GroupMemberRepository;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.domain.auth.repository.UserRepository;
import com.app.boot_app.domain.auth.security.SecurityUtils;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import com.app.boot_app.shared.infra.email.Email;

@Service
public class GroupInvitationServiceImpl implements GroupInvitationService {

    private final GroupInvitationRepository invitationRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final Email emailService;
    private final SecurityUtils securityUtils;
    private final MessageSource messageSource;

    public GroupInvitationServiceImpl(GroupInvitationRepository invitationRepository, GroupRepository groupRepository,
            UserRepository userRepository, RoleRepository roleRepository, GroupMemberRepository groupMemberRepository,
            Email emailService, SecurityUtils securityUtils, MessageSource messageSource) {
        this.invitationRepository = invitationRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.emailService = emailService;
        this.securityUtils = securityUtils;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public GroupInvitationResponseDTO sendInvitation(UUID groupId, GroupInvitationRequestDTO invitationRequestDTO) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));

        if (invitationRepository.findByGroupAndEmailAndStatus(group, invitationRequestDTO.getEmail(),
                GroupInvitation.InvitationStatus.PENDING).isPresent()) {
            throw new ConflictException("pending-invitation-already-exists",
                    messageSource.getMessage("group.invitation.pending.exists", null, LocaleContextHolder.getLocale()));
        }

        User invitedBy = securityUtils.getAuthenticatedUser();

        Role role = null;
        if (invitationRequestDTO.getRoleId() != null) {
            role = roleRepository.findById(invitationRequestDTO.getRoleId())
                    .orElseThrow(() -> new NotFoundException("role-not-found",
                            messageSource.getMessage("role.not.found", null, LocaleContextHolder.getLocale())));
        }

        GroupInvitation invitation = new GroupInvitation();
        invitation.setGroup(group);
        invitation.setEmail(invitationRequestDTO.getEmail());
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setRole(role);
        invitation.setInvitedBy(invitedBy);
        invitation.setCreatedAt(LocalDateTime.now());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7));
        invitation.setStatus(GroupInvitation.InvitationStatus.PENDING);

        invitation = invitationRepository.save(invitation);

        // TODO: Send invitation email
        String emailSubject = messageSource.getMessage("group.invitation.email.subject",
                new Object[] { group.getName() }, LocaleContextHolder.getLocale());
        String emailBody = messageSource.getMessage("group.invitation.email.body",
                new Object[] { group.getName(), invitation.getToken() }, LocaleContextHolder.getLocale());
        emailService.sendEmail(invitation.getEmail(), emailSubject, emailBody);

        return convertToDto(invitation);
    }

    @Override
    public GroupInvitationResponseDTO validateInvitation(String token) {
        GroupInvitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("invitation-not-found",
                        messageSource.getMessage("group.invitation.not.found", null, LocaleContextHolder.getLocale())));

        if (invitation.getStatus() != GroupInvitation.InvitationStatus.PENDING) {
            throw new ConflictException("invitation-not-pending",
                    messageSource.getMessage("group.invitation.not.pending", null, LocaleContextHolder.getLocale()));
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            invitation.setStatus(GroupInvitation.InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new ConflictException("invitation-expired",
                    messageSource.getMessage("group.invitation.expired", null, LocaleContextHolder.getLocale()));
        }

        return convertToDto(invitation);
    }

    @Override
    @Transactional
    public GroupInvitationResponseDTO acceptInvitation(String token, UUID userId) {
        GroupInvitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("invitation-not-found",
                        messageSource.getMessage("group.invitation.not.found", null, LocaleContextHolder.getLocale())));

        if (invitation.getStatus() != GroupInvitation.InvitationStatus.PENDING) {
            throw new ConflictException("invitation-not-pending",
                    messageSource.getMessage("group.invitation.not.pending", null, LocaleContextHolder.getLocale()));
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            invitation.setStatus(GroupInvitation.InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new ConflictException("invitation-expired",
                    messageSource.getMessage("group.invitation.expired", null, LocaleContextHolder.getLocale()));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user-not-found",
                        messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));

        if (!user.getEmail().equalsIgnoreCase(invitation.getEmail())) {
            throw new ConflictException("user-email-does-not-match",
                    messageSource.getMessage("user.email.does.not.match", null, LocaleContextHolder.getLocale()));
        }

        if (groupMemberRepository.findByGroupAndUser(invitation.getGroup(), user).isPresent()) {
            throw new ConflictException("user-already-member-of-this-group", messageSource
                    .getMessage("user.already.member.of.this.group", null, LocaleContextHolder.getLocale()));
        }

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(invitation.getGroup());
        groupMember.setUser(user);
        groupMember.setAssignedRole(invitation.getRole());
        groupMember.setJoinedAt(LocalDateTime.now());
        groupMember.setIsActive(true);
        groupMemberRepository.save(groupMember);

        invitation.setStatus(GroupInvitation.InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);

        return convertToDto(invitation);
    }

    @Override
    public void cancelInvitation(UUID invitationId) {
        GroupInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("invitation-not-found",
                        messageSource.getMessage("group.invitation.not.found", null, LocaleContextHolder.getLocale())));

        if (invitation.getStatus() != GroupInvitation.InvitationStatus.PENDING) {
            throw new ConflictException("only-pending-invitations-can-be-cancelled", messageSource
                    .getMessage("only.pending.invitations.can.be.cancelled", null, LocaleContextHolder.getLocale()));
        }

        invitation.setStatus(GroupInvitation.InvitationStatus.CANCELLED);
        invitationRepository.save(invitation);
    }

    private GroupInvitationResponseDTO convertToDto(GroupInvitation invitation) {
        GroupInvitationResponseDTO dto = new GroupInvitationResponseDTO();
        dto.setId(invitation.getId());
        dto.setGroupId(invitation.getGroup().getId());
        dto.setEmail(invitation.getEmail());
        dto.setToken(invitation.getToken());
        if (invitation.getRole() != null) {
            dto.setRoleId(invitation.getRole().getId());
        }
        dto.setInvitedBy(invitation.getInvitedBy().getId());
        dto.setCreatedAt(invitation.getCreatedAt());
        dto.setExpiresAt(invitation.getExpiresAt());
        dto.setStatus(invitation.getStatus().name());
        return dto;
    }
}
