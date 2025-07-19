package com.app.boot_app.domain.auth.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.dto.GroupMemberRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupMemberResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupMember;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.exception.ConflictException;
import com.app.boot_app.domain.auth.exception.NotFoundException;
import com.app.boot_app.domain.auth.repository.GroupMemberRepository;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.domain.auth.repository.UserRepository;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    public GroupMemberServiceImpl(GroupMemberRepository groupMemberRepository, GroupRepository groupRepository,
            UserRepository userRepository, RoleRepository roleRepository, MessageSource messageSource) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
    }

    @Override
    public GroupMemberResponseDTO addMemberToGroup(UUID groupId, GroupMemberRequestDTO groupMemberRequestDTO) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));

        User user = userRepository.findById(groupMemberRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("user-not-found",
                        messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));

        if (groupMemberRepository.findByGroupAndUser(group, user).isPresent()) {
            throw new ConflictException("user-already-member-of-group", messageSource
                    .getMessage("user.already.member.of.this.group", null, LocaleContextHolder.getLocale()));
        }

        Role assignedRole = null;
        if (groupMemberRequestDTO.getAssignedRoleId() != null) {
            assignedRole = roleRepository.findById(groupMemberRequestDTO.getAssignedRoleId())
                    .orElseThrow(() -> new NotFoundException("assigned-role-not-found",
                            messageSource.getMessage("role.not.found", null, LocaleContextHolder.getLocale())));
        }

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setAssignedRole(assignedRole);
        groupMember.setJoinedAt(LocalDateTime.now());
        groupMember.setIsActive(true);

        groupMember = groupMemberRepository.save(groupMember);
        return convertToDto(groupMember);
    }

    @Override
    public GroupMemberResponseDTO updateMemberRole(UUID groupId, UUID userId,
            GroupMemberRequestDTO groupMemberRequestDTO) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user-not-found",
                        messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));

        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new NotFoundException("user-not-member-of-this-group", messageSource
                        .getMessage("user.not.member.of.this.group", null, LocaleContextHolder.getLocale())));

        Role assignedRole = null;
        if (groupMemberRequestDTO.getAssignedRoleId() != null) {
            assignedRole = roleRepository.findById(groupMemberRequestDTO.getAssignedRoleId())
                    .orElseThrow(() -> new NotFoundException("assigned-role-not-found",
                            messageSource.getMessage("role.not.found", null, LocaleContextHolder.getLocale())));
        }

        groupMember.setAssignedRole(assignedRole);
        groupMember = groupMemberRepository.save(groupMember);
        return convertToDto(groupMember);
    }

    @Override
    public void removeMemberFromGroup(UUID groupId, UUID userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user-not-found",
                        messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));

        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new NotFoundException("user-not-member-of-this-group", messageSource
                        .getMessage("user.not.member.of.this.group", null, LocaleContextHolder.getLocale())));

        groupMemberRepository.delete(groupMember);
    }

    @Override
    public List<GroupMemberResponseDTO> getMembersOfGroup(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));

        return groupMemberRepository.findByGroup(group).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private GroupMemberResponseDTO convertToDto(GroupMember groupMember) {
        GroupMemberResponseDTO dto = new GroupMemberResponseDTO();
        dto.setId(groupMember.getId());
        dto.setGroupId(groupMember.getGroup().getId());
        dto.setUserId(groupMember.getUser().getId());
        if (groupMember.getAssignedRole() != null) {
            dto.setAssignedRoleId(groupMember.getAssignedRole().getId());
        }
        dto.setJoinedAt(groupMember.getJoinedAt());
        if (groupMember.getInvitedBy() != null) {
            dto.setInvitedBy(groupMember.getInvitedBy().getId());
        }
        dto.setInvitationExpiresAt(groupMember.getInvitationExpiresAt());
        dto.setIsActive(groupMember.getIsActive());
        return dto;
    }
}
