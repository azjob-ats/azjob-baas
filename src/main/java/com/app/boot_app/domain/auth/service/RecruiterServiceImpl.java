package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateRecruiterRequestDTO;
import com.app.boot_app.domain.auth.dto.RecruiterResponseDTO;
import com.app.boot_app.domain.auth.entity.Recruiter;
import com.app.boot_app.domain.auth.mapper.RecruiterMapper;
import com.app.boot_app.domain.auth.repository.RecruiterRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final RecruiterMapper recruiterMapper = RecruiterMapper.INSTANCE;

    public RecruiterServiceImpl(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public List<RecruiterResponseDTO> findAll() {
        return recruiterRepository.findAll().stream()
                .map(recruiterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecruiterResponseDTO findById(UUID id) {
        Recruiter recruiter = recruiterRepository.findById(id).orElseThrow(() -> new NotFoundException("not_found", "Recruiter not found"));
        return recruiterMapper.toDto(recruiter);
    }

    @Override
    public RecruiterResponseDTO create(CreateRecruiterRequestDTO dto) {
        Recruiter recruiter = recruiterMapper.toEntity(dto);
        return recruiterMapper.toDto(recruiterRepository.save(recruiter));
    }

    @Override
    public void delete(UUID id) {
        recruiterRepository.deleteById(id);
    }
}
