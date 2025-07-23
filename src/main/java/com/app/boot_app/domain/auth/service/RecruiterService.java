package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateRecruiterRequestDTO;
import com.app.boot_app.domain.auth.dto.RecruiterResponseDTO;

import java.util.List;
import java.util.UUID;

public interface RecruiterService {
    List<RecruiterResponseDTO> findAll();
    RecruiterResponseDTO findById(UUID id);
    RecruiterResponseDTO create(CreateRecruiterRequestDTO dto);
    void delete(UUID id);
}
