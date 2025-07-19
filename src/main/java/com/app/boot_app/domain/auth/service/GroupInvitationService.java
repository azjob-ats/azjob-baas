package com.app.boot_app.domain.auth.service;

import java.util.UUID;

import com.app.boot_app.domain.auth.dto.GroupInvitationRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupInvitationResponseDTO;

public interface GroupInvitationService {
    GroupInvitationResponseDTO sendInvitation(UUID groupId, GroupInvitationRequestDTO invitationRequestDTO);
    GroupInvitationResponseDTO validateInvitation(String token);
    GroupInvitationResponseDTO acceptInvitation(String token, UUID userId);
    void cancelInvitation(UUID invitationId);
}
