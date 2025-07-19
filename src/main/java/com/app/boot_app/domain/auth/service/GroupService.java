package com.app.boot_app.domain.auth.service;

import java.util.List;
import java.util.UUID;

import com.app.boot_app.domain.auth.dto.GroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;

public interface GroupService {
    GroupResponseDTO createGroup(GroupRequestDTO groupRequestDTO);
    GroupResponseDTO getGroupById(UUID id);
    List<GroupResponseDTO> getAllGroups();
    GroupResponseDTO updateGroup(UUID id, GroupRequestDTO groupRequestDTO);
    void deleteGroup(UUID id);
}
