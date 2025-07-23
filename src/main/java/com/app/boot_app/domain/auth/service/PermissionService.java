package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreatePermissionRequestDTO;
import com.app.boot_app.domain.auth.dto.PermissionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PermissionService {
    List<PermissionResponseDTO> findAll();
    PermissionResponseDTO findById(UUID id);
    PermissionResponseDTO create(CreatePermissionRequestDTO dto);
    void delete(UUID id);
}
