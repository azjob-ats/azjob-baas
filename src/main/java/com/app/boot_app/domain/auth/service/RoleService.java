package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateRoleRequestDTO;
import com.app.boot_app.domain.auth.dto.RoleResponseDTO;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleResponseDTO> findAll();
    RoleResponseDTO findById(UUID id);
    RoleResponseDTO create(CreateRoleRequestDTO dto);
}
