package com.app.boot_app.domain.auth.controller;

import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.boot_app.core.security.SecurityUtils;
import com.app.boot_app.domain.auth.dto.GroupInvitationRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupInvitationResponseDTO;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.service.GroupInvitationService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;

import jakarta.validation.Valid;

@RestController
public class GroupInvitationController {

    private final GroupInvitationService invitationService;
    private final SecurityUtils securityUtils;
    private final MessageSource messageSource;

    public GroupInvitationController(GroupInvitationService invitationService, SecurityUtils securityUtils,
            MessageSource messageSource) {
        this.invitationService = invitationService;
        this.securityUtils = securityUtils;
        this.messageSource = messageSource;
    }

    @PostMapping("/api/v1/groups/{groupId}/invitations")
    public ApiResponse<GroupInvitationResponseDTO> sendInvitation(@PathVariable UUID groupId,
            @Valid @RequestBody GroupInvitationRequestDTO invitationRequestDTO) {
        GroupInvitationResponseDTO result = invitationService.sendInvitation(groupId, invitationRequestDTO);
        return Response.ok(messageSource.getMessage("group.invitation.sent", null, LocaleContextHolder.getLocale()),
                result);
    }

    @GetMapping("/api/v1/invitations/{token}")
    public ApiResponse<GroupInvitationResponseDTO> validateInvitation(@PathVariable String token) {
        GroupInvitationResponseDTO result = invitationService.validateInvitation(token);
        return Response.ok(
                messageSource.getMessage("group.invitation.validated", null, LocaleContextHolder.getLocale()), result);
    }

    @PostMapping("/api/v1/invitations/{token}/accept")
    public ApiResponse<GroupInvitationResponseDTO> acceptInvitation(@PathVariable String token) {
        User user = securityUtils.getAuthenticatedUser();
        GroupInvitationResponseDTO result = invitationService.acceptInvitation(token, user.getId());
        return Response.ok(messageSource.getMessage("group.invitation.accepted", null, LocaleContextHolder.getLocale()),
                result);
    }

    @DeleteMapping("/api/v1/invitations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> cancelInvitation(@PathVariable UUID id) {
        invitationService.cancelInvitation(id);
        return Response.ok(
                messageSource.getMessage("group.invitation.cancelled", null, LocaleContextHolder.getLocale()), null);
    }
}
