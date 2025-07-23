package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.CreateUserGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.UserGroupResponseDTO;
import com.app.boot_app.domain.auth.service.UserGroupService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-groups")
public class UserGroupController {

    private final UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @GetMapping
    public ApiResponse<List<UserGroupResponseDTO>> findAll() {
        return Response.ok("UserGroups retrieved successfully", userGroupService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserGroupResponseDTO> findById(@PathVariable UUID id) {
        return Response.ok("UserGroup retrieved successfully", userGroupService.findById(id));
    }

    @PostMapping
    public ApiResponse<UserGroupResponseDTO> create(@Valid @RequestBody CreateUserGroupRequestDTO dto) {
        return Response.created("UserGroup created successfully", userGroupService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        userGroupService.delete(id);
        return Response.noContent();
    }
}
