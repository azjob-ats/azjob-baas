package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateUserGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.UserGroupResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserGroupService {
    List<UserGroupResponseDTO> findAll();
    UserGroupResponseDTO findById(UUID id);
    UserGroupResponseDTO create(CreateUserGroupRequestDTO dto);
    void delete(UUID id);
}
