package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    List<GroupResponseDTO> findAll();
    GroupResponseDTO findById(UUID id);
    GroupResponseDTO create(CreateGroupRequestDTO dto);
    void delete(UUID id);
}
