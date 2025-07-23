package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateEnterpriseRequestDTO;
import com.app.boot_app.domain.auth.dto.EnterpriseResponseDTO;
import com.app.boot_app.domain.auth.entity.Enterprise;
import com.app.boot_app.domain.auth.mapper.EnterpriseMapper;
import com.app.boot_app.domain.auth.repository.EnterpriseRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper = EnterpriseMapper.INSTANCE;

    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    public List<EnterpriseResponseDTO> findAll() {
        return enterpriseRepository.findAll().stream()
                .map(enterpriseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EnterpriseResponseDTO findById(UUID id) {
        Enterprise enterprise = enterpriseRepository.findById(id).orElseThrow(() -> new NotFoundException("not_found", "Enterprise not found"));
        return enterpriseMapper.toDto(enterprise);
    }

    @Override
    public EnterpriseResponseDTO create(CreateEnterpriseRequestDTO dto) {
        Enterprise enterprise = enterpriseMapper.toEntity(dto);
        return enterpriseMapper.toDto(enterpriseRepository.save(enterprise));
    }

    @Override
    public void delete(UUID id) {
        enterpriseRepository.deleteById(id);
    }
}
