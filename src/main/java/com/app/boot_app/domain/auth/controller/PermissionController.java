package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.GrantPermissionToGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GrantPermissionToRoleRequestDTO;
import com.app.boot_app.domain.auth.dto.UserHasPermissionRequestDTO;
import com.app.boot_app.domain.auth.dto.UserHasPermissionResponseDTO;
import com.app.boot_app.domain.auth.service.PermissionService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/role/grant")
    public ApiResponse<Void> grantPermissionToRole(@Valid @RequestBody GrantPermissionToRoleRequestDTO requestDTO) {
        permissionService.grantPermissionToRole(requestDTO.getRoleId(), requestDTO.getActionId(), requestDTO.getEnterpriseId());
        return Response.ok("Permission granted to role successfully", null);
    }

    @PostMapping("/role/revoke")
    public ApiResponse<Void> revokePermissionFromRole(@Valid @RequestBody GrantPermissionToRoleRequestDTO requestDTO) {
        permissionService.revokePermissionFromRole(requestDTO.getRoleId(), requestDTO.getActionId(), requestDTO.getEnterpriseId());
        return Response.ok("Permission revoked from role successfully", null);
    }

    @PostMapping("/group/grant")
    public ApiResponse<Void> grantPermissionToGroup(@Valid @RequestBody GrantPermissionToGroupRequestDTO requestDTO) {
        permissionService.grantPermissionToGroup(requestDTO.getGroupId(), requestDTO.getActionId(), requestDTO.getEnterpriseId());
        return Response.ok("Permission granted to group successfully", null);
    }

    @PostMapping("/group/revoke")
    public ApiResponse<Void> revokePermissionFromGroup(@Valid @RequestBody GrantPermissionToGroupRequestDTO requestDTO) {
        permissionService.revokePermissionFromGroup(requestDTO.getGroupId(), requestDTO.getActionId(), requestDTO.getEnterpriseId());
        return Response.ok("Permission revoked from group successfully", null);
    }

    @PostMapping("/user/check")
    public ApiResponse<UserHasPermissionResponseDTO> userHasPermissionForAction(@Valid @RequestBody UserHasPermissionRequestDTO requestDTO) {
        boolean hasPermission = permissionService.userHasPermissionForAction(requestDTO.getUserId(), requestDTO.getActionId(), requestDTO.getEnterpriseId());
        return Response.ok("Permission check completed", new UserHasPermissionResponseDTO(hasPermission));
    }
}
