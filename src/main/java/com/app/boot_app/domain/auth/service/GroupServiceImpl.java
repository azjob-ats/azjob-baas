package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.mapper.GroupMapper;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupResponseDTO> findAll() {
        return groupRepository.findAll().stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupResponseDTO findById(UUID id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("not_found", "Group not found"));
        return groupMapper.toDto(group);
    }

    @Override
    public GroupResponseDTO create(CreateGroupRequestDTO dto) {
        Group group = groupMapper.toEntity(dto);
        return groupMapper.toDto(groupRepository.save(group));
    }

    @Override
    public void delete(UUID id) {
        groupRepository.deleteById(id);
    }
}
