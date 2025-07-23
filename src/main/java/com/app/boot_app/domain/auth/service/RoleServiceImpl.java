package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateRoleRequestDTO;
import com.app.boot_app.domain.auth.dto.RoleResponseDTO;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.mapper.RoleMapper;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper = RoleMapper.INSTANCE;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleResponseDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDTO findById(UUID id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("not_found", "Role not found"));
        return roleMapper.toDto(role);
    }

    @Override
    public RoleResponseDTO create(CreateRoleRequestDTO dto) {
        Role role = roleMapper.toEntity(dto);
        return roleMapper.toDto(roleRepository.save(role));
    }
}
