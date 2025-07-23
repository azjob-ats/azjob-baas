package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.ActionResponseDTO;
import com.app.boot_app.domain.auth.dto.CreateActionRequestDTO;

import java.util.List;
import java.util.UUID;

public interface ActionService {
    List<ActionResponseDTO> findAll();
    ActionResponseDTO findById(UUID id);
    ActionResponseDTO create(CreateActionRequestDTO dto);
}
