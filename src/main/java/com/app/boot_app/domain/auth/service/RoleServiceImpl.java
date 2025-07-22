
package com.app.boot_app.domain.auth.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.dto.RoleRequestDTO;
import com.app.boot_app.domain.auth.dto.RoleResponseDTO;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    public RoleServiceImpl(RoleRepository roleRepository, MessageSource messageSource) {
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
    }

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        if (roleRepository.findByName(roleRequestDTO.getName().toUpperCase()).isPresent()) {
                        throw new ConflictException("role-already-exists", messageSource.getMessage("role.already.exists", null, LocaleContextHolder.getLocale()));
        }

        Role role = new Role();
        role.setName(roleRequestDTO.getName().toUpperCase());
        role.setDescription(roleRequestDTO.getDescription());
        role.setPermissions(roleRequestDTO.getPermissions());
        role.setIsDefault(roleRequestDTO.getIsDefault());
        role.setLevel(roleRequestDTO.getLevel());
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());

        role = roleRepository.save(role);
        return convertToDto(role);
    }

    @Override
    public RoleResponseDTO getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("role-not-found", messageSource.getMessage("role.not.found", null, LocaleContextHolder.getLocale())));
        return convertToDto(role);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDTO updateRole(UUID id, RoleRequestDTO roleRequestDTO) {
        Role role = roleRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("role-not-found", messageSource.getMessage("role.not.found", null, LocaleContextHolder.getLocale())));

        if (roleRepository.findByName(roleRequestDTO.getName().toUpperCase()).isPresent() && !role.getName().equalsIgnoreCase(roleRequestDTO.getName())) {
                        throw new ConflictException("role-already-exists", messageSource.getMessage("role.already.exists", null, LocaleContextHolder.getLocale()));
        }

        role.setName(roleRequestDTO.getName().toUpperCase());
        role.setDescription(roleRequestDTO.getDescription());
        role.setPermissions(roleRequestDTO.getPermissions());
        role.setIsDefault(roleRequestDTO.getIsDefault());
        role.setLevel(roleRequestDTO.getLevel());
        role.setUpdatedAt(LocalDateTime.now());

        role = roleRepository.save(role);
        return convertToDto(role);
    }

    @Override
    public void deleteRole(UUID id) {
        Role role = roleRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("role-not-found", messageSource.getMessage("role.not.found", null, LocaleContextHolder.getLocale())));
        roleRepository.delete(role);
    }

    private RoleResponseDTO convertToDto(Role role) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setPermissions(role.getPermissions());
        dto.setIsDefault(role.getIsDefault());
        dto.setLevel(role.getLevel());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        return dto;
    }
}
