package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.dto.CreateUserGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.UserGroupResponseDTO;
import com.app.boot_app.domain.auth.entity.UserGroup;
import com.app.boot_app.domain.auth.mapper.UserGroupMapper;
import com.app.boot_app.domain.auth.repository.UserGroupRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserGroupMapper userGroupMapper = UserGroupMapper.INSTANCE;

    public UserGroupServiceImpl(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public List<UserGroupResponseDTO> findAll() {
        return userGroupRepository.findAll().stream()
                .map(userGroupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserGroupResponseDTO findById(UUID id) {
        UserGroup userGroup = userGroupRepository.findById(id).orElseThrow(() -> new NotFoundException("not_found", "UserGroup not found"));
        return userGroupMapper.toDto(userGroup);
    }

    @Override
    public UserGroupResponseDTO create(CreateUserGroupRequestDTO dto) {
        UserGroup userGroup = userGroupMapper.toEntity(dto);
        return userGroupMapper.toDto(userGroupRepository.save(userGroup));
    }

    @Override
    public void delete(UUID id) {
        userGroupRepository.deleteById(id);
    }
}
