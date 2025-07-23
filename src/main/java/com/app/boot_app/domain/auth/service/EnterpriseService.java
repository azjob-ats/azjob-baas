package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateEnterpriseRequestDTO;
import com.app.boot_app.domain.auth.dto.EnterpriseResponseDTO;

import java.util.List;
import java.util.UUID;

public interface EnterpriseService {
    List<EnterpriseResponseDTO> findAll();
    EnterpriseResponseDTO findById(UUID id);
    EnterpriseResponseDTO create(CreateEnterpriseRequestDTO dto);
    void delete(UUID id);
}
