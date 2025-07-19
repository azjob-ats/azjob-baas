package com.app.boot_app.domain.auth.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.boot_app.domain.auth.dto.GroupMemberRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupMemberResponseDTO;
import com.app.boot_app.domain.auth.dto.GroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.service.GroupMemberService;
import com.app.boot_app.domain.auth.service.GroupService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final MessageSource messageSource;

    public GroupController(GroupService groupService, GroupMemberService groupMemberService,
            MessageSource messageSource) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ApiResponse<GroupResponseDTO> createGroup(@Valid @RequestBody GroupRequestDTO groupRequestDTO) {
        GroupResponseDTO result = groupService.createGroup(groupRequestDTO);
        return Response.ok(messageSource.getMessage("group.created", null, LocaleContextHolder.getLocale()), result);
    }

    @GetMapping("/{id}")
    public ApiResponse<GroupResponseDTO> getGroupById(@PathVariable UUID id) {
        GroupResponseDTO result = groupService.getGroupById(id);
        return Response.ok(messageSource.getMessage("group.found", null, LocaleContextHolder.getLocale()), result);
    }

    @GetMapping
    public ApiResponse<List<GroupResponseDTO>> getAllGroups() {
        List<GroupResponseDTO> result = groupService.getAllGroups();
        return Response.ok(messageSource.getMessage("group.listed", null, LocaleContextHolder.getLocale()), result);
    }

    @PutMapping("/{id}")
    public ApiResponse<GroupResponseDTO> updateGroup(@PathVariable UUID id,
            @Valid @RequestBody GroupRequestDTO groupRequestDTO) {
        GroupResponseDTO result = groupService.updateGroup(id, groupRequestDTO);
        return Response.ok(messageSource.getMessage("group.updated", null, LocaleContextHolder.getLocale()), result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> deleteGroup(@PathVariable UUID id) {
        groupService.deleteGroup(id);
        return Response.ok(messageSource.getMessage("group.deleted", null, LocaleContextHolder.getLocale()), null);
    }

    @PostMapping("/{groupId}/members")
    public ApiResponse<GroupMemberResponseDTO> addMemberToGroup(@PathVariable UUID groupId,
            @Valid @RequestBody GroupMemberRequestDTO groupMemberRequestDTO) {
        GroupMemberResponseDTO result = groupMemberService.addMemberToGroup(groupId, groupMemberRequestDTO);
        return Response.ok(messageSource.getMessage("group.member.added", null, LocaleContextHolder.getLocale()),
                result);
    }

    @PutMapping("/{groupId}/members/{userId}")
    public ApiResponse<GroupMemberResponseDTO> updateMemberRole(@PathVariable UUID groupId, @PathVariable UUID userId,
            @Valid @RequestBody GroupMemberRequestDTO groupMemberRequestDTO) {
        GroupMemberResponseDTO result = groupMemberService.updateMemberRole(groupId, userId, groupMemberRequestDTO);
        return Response.ok(messageSource.getMessage("group.member.role.updated", null, LocaleContextHolder.getLocale()),
                result);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> removeMemberFromGroup(@PathVariable UUID groupId, @PathVariable UUID userId) {
        groupMemberService.removeMemberFromGroup(groupId, userId);
        return Response.ok(messageSource.getMessage("group.member.removed", null, LocaleContextHolder.getLocale()),
                null);
    }

    @GetMapping("/{groupId}/members")
    public ApiResponse<List<GroupMemberResponseDTO>> getMembersOfGroup(@PathVariable UUID groupId) {
        List<GroupMemberResponseDTO> result = groupMemberService.getMembersOfGroup(groupId);
        return Response.ok(messageSource.getMessage("group.members.listed", null, LocaleContextHolder.getLocale()),
                result);
    }
}
