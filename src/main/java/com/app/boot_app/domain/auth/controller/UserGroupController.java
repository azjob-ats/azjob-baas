package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.dto.UserResponseDTO;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.mapper.GroupMapper;
import com.app.boot_app.domain.auth.mapper.UserMapper;
import com.app.boot_app.domain.auth.dto.AddUserToGroupRequestDTO;
import com.app.boot_app.domain.auth.service.UserGroupService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/user-groups")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;

    @PostMapping("/add")
    public ApiResponse<Void> addUserToGroup(@Valid @RequestBody AddUserToGroupRequestDTO requestDTO) {
        userGroupService.addUserToGroup(requestDTO.getUserId(), requestDTO.getGroupId(), requestDTO.getEnterpriseId());
        return Response.ok("User added to group successfully", null);
    }

    @PostMapping("/remove")
    public ApiResponse<Void> removeUserFromGroup(@Valid @RequestBody AddUserToGroupRequestDTO requestDTO) {
        userGroupService.removeUserFromGroup(requestDTO.getUserId(), requestDTO.getGroupId(), requestDTO.getEnterpriseId());
        return Response.ok("User removed from group successfully", null);
    }

    @GetMapping("/group/{groupId}")
    public ApiResponse<List<UserResponseDTO>> listUsersInGroup(@PathVariable UUID groupId) {
        List<User> users = userGroupService.listUsersInGroup(groupId);
        return Response.ok("Users in group retrieved successfully", userMapper.usersToUserResponseDTOs(users));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<GroupResponseDTO>> listGroupsForUser(@PathVariable UUID userId) {
        List<com.app.boot_app.domain.auth.entity.Group> groups = userGroupService.listGroupsForUser(userId);
        return Response.ok("Groups for user retrieved successfully", groupMapper.groupsToGroupResponseDTOs(groups));
    }
}
