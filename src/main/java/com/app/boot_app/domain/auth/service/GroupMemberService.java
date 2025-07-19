package com.app.boot_app.domain.auth.service;

import java.util.List;
import java.util.UUID;

import com.app.boot_app.domain.auth.dto.GroupMemberRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupMemberResponseDTO;

public interface GroupMemberService {
    GroupMemberResponseDTO addMemberToGroup(UUID groupId, GroupMemberRequestDTO groupMemberRequestDTO);
    GroupMemberResponseDTO updateMemberRole(UUID groupId, UUID userId, GroupMemberRequestDTO groupMemberRequestDTO);
    void removeMemberFromGroup(UUID groupId, UUID userId);
    List<GroupMemberResponseDTO> getMembersOfGroup(UUID groupId);
}
