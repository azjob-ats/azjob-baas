package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.CreateGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.mapper.GroupMapper;
import com.app.boot_app.domain.auth.service.GroupService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @PostMapping
    public ApiResponse<GroupResponseDTO> createGroup(@Valid @RequestBody CreateGroupRequestDTO requestDTO) {
        Group group = groupService.createGroupForEnterprise(requestDTO.getName(), requestDTO.getDescription(), requestDTO.getEnterpriseId());
        return Response.ok("Group created successfully", groupMapper.groupToGroupResponseDTO(group));
    }

    @GetMapping("/enterprise/{enterpriseId}")
    public ApiResponse<List<GroupResponseDTO>> listGroupsByEnterprise(@PathVariable UUID enterpriseId) {
        List<Group> groups = groupService.listGroupsByEnterprise(enterpriseId);
        return Response.ok("Groups retrieved successfully", groupMapper.groupsToGroupResponseDTOs(groups));
    }

    @DeleteMapping("/{groupId}/enterprise/{enterpriseId}")
    public ApiResponse<Void> deactivateGroup(@PathVariable UUID groupId, @PathVariable UUID enterpriseId) {
        groupService.deactivateGroup(groupId, enterpriseId);
        return Response.ok("Group deactivated successfully", null);
    }
}
