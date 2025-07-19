package com.app.boot_app.domain.auth.service;

import java.util.List;
import java.util.UUID;

import com.app.boot_app.domain.auth.dto.RoleRequestDTO;
import com.app.boot_app.domain.auth.dto.RoleResponseDTO;

public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);
    RoleResponseDTO getRoleById(UUID id);
    List<RoleResponseDTO> getAllRoles();
    RoleResponseDTO updateRole(UUID id, RoleRequestDTO roleRequestDTO);
    void deleteRole(UUID id);
}
