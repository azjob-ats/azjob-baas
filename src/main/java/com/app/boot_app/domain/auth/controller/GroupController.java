package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.CreateGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.service.GroupService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ApiResponse<List<GroupResponseDTO>> findAll() {
        return Response.ok("Groups retrieved successfully", groupService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<GroupResponseDTO> findById(@PathVariable UUID id) {
        return Response.ok("Group retrieved successfully", groupService.findById(id));
    }

    @PostMapping
    public ApiResponse<GroupResponseDTO> create(@Valid @RequestBody CreateGroupRequestDTO dto) {
        return Response.created("Group created successfully", groupService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        groupService.delete(id);
        return Response.noContent();
    }
}
