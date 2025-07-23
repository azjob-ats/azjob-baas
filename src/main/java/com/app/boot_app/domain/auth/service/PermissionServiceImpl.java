package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreatePermissionRequestDTO;
import com.app.boot_app.domain.auth.dto.PermissionResponseDTO;
import com.app.boot_app.domain.auth.entity.Permission;
import com.app.boot_app.domain.auth.mapper.PermissionMapper;
import com.app.boot_app.domain.auth.repository.PermissionRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper = PermissionMapper.INSTANCE;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<PermissionResponseDTO> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionResponseDTO findById(UUID id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new NotFoundException("not_found", "Permission not found"));
        return permissionMapper.toDto(permission);
    }

    @Override
    public PermissionResponseDTO create(CreatePermissionRequestDTO dto) {
        Permission permission = permissionMapper.toEntity(dto);
        return permissionMapper.toDto(permissionRepository.save(permission));
    }

    @Override
    public void delete(UUID id) {
        permissionRepository.deleteById(id);
    }
}
