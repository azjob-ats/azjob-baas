package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.ActionResponseDTO;
import com.app.boot_app.domain.auth.dto.CreateActionRequestDTO;
import com.app.boot_app.domain.auth.entity.Action;
import com.app.boot_app.domain.auth.mapper.ActionMapper;
import com.app.boot_app.domain.auth.repository.ActionRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper = ActionMapper.INSTANCE;

    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public List<ActionResponseDTO> findAll() {
        return actionRepository.findAll().stream()
                .map(actionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ActionResponseDTO findById(UUID id) {
        Action action = actionRepository.findById(id).orElseThrow(() -> new NotFoundException("not_found", "Action not found"));
        return actionMapper.toDto(action);
    }

    @Override
    public ActionResponseDTO create(CreateActionRequestDTO dto) {
        Action action = actionMapper.toEntity(dto);
        return actionMapper.toDto(actionRepository.save(action));
    }
}
