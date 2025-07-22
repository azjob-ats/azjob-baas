package com.app.boot_app.domain.auth.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.dto.GroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.domain.auth.repository.UserRepository;
import com.app.boot_app.domain.auth.security.SecurityUtils;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.NotFoundException;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityUtils securityUtils;
    private final MessageSource messageSource;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository,
            RoleRepository roleRepository, SecurityUtils securityUtils, MessageSource messageSource) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityUtils = securityUtils;
        this.messageSource = messageSource;
    }

    @Override
    public GroupResponseDTO createGroup(GroupRequestDTO groupRequestDTO) {
        if (groupRepository.findByName(groupRequestDTO.getName()).isPresent()) {
            throw new ConflictException("group-already-exists",
                    messageSource.getMessage("group.already.exists", null, LocaleContextHolder.getLocale()));
        }

        User createdBy = securityUtils.getAuthenticatedUser();

        Role defaultRole = null;
        if (groupRequestDTO.getDefaultRoleId() != null) {
            defaultRole = roleRepository.findById(groupRequestDTO.getDefaultRoleId())
                    .orElseThrow(() -> new NotFoundException("default-role-not-found", messageSource
                            .getMessage("group.default.role.not.found", null, LocaleContextHolder.getLocale())));
        }

        Group group = new Group();
        group.setName(groupRequestDTO.getName());
        group.setDescription(groupRequestDTO.getDescription());
        group.setDefaultRole(defaultRole);
        group.setCreatedBy(createdBy);
        group.setCreatedAt(LocalDateTime.now());
        group.setUpdatedAt(LocalDateTime.now());
        group.setIsActive(true);

        group = groupRepository.save(group);
        return convertToDto(group);
    }

    @Override
    public GroupResponseDTO getGroupById(UUID id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));
        return convertToDto(group);
    }

    @Override
    public List<GroupResponseDTO> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupResponseDTO updateGroup(UUID id, GroupRequestDTO groupRequestDTO) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));

        if (groupRepository.findByName(groupRequestDTO.getName()).isPresent()
                && !group.getName().equals(groupRequestDTO.getName())) {
            throw new ConflictException("group-already-exists",
                    messageSource.getMessage("group.already.exists", null, LocaleContextHolder.getLocale()));
        }

        Role defaultRole = null;
        if (groupRequestDTO.getDefaultRoleId() != null) {
            defaultRole = roleRepository.findById(groupRequestDTO.getDefaultRoleId())
                    .orElseThrow(() -> new NotFoundException("default-role-not-found", messageSource
                            .getMessage("group.default.role.not.found", null, LocaleContextHolder.getLocale())));
        }

        group.setName(groupRequestDTO.getName());
        group.setDescription(groupRequestDTO.getDescription());
        group.setDefaultRole(defaultRole);
        group.setUpdatedAt(LocalDateTime.now());

        group = groupRepository.save(group);
        return convertToDto(group);
    }

    @Override
    public void deleteGroup(UUID id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("group-not-found",
                        messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));
        groupRepository.delete(group);
    }

    private GroupResponseDTO convertToDto(Group group) {
        GroupResponseDTO dto = new GroupResponseDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        if (group.getDefaultRole() != null) {
            dto.setDefaultRoleId(group.getDefaultRole().getId());
        }
        dto.setCreatedAt(group.getCreatedAt());
        dto.setUpdatedAt(group.getUpdatedAt());
        dto.setCreatedBy(group.getCreatedBy().getId());
        dto.setIsActive(group.getIsActive());
        return dto;
    }
}
