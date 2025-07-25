package com.app.boot_app.domain.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.entity.Action;
import com.app.boot_app.domain.auth.entity.Enterprise;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.Permission;
import com.app.boot_app.domain.auth.entity.Role;
import com.app.boot_app.domain.auth.repository.ActionRepository;
import com.app.boot_app.domain.auth.repository.EnterpriseRepository;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.domain.auth.repository.PermissionRepository;
import com.app.boot_app.domain.auth.repository.RoleRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;
    private final GroupRepository groupRepository;
    private final EnterpriseRepository enterpriseRepository;

    @Override
    public void grantPermissionToRole(UUID roleId, UUID actionId, UUID enterpriseId, UUID groupId) {
        
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new NotFoundException("Permission/grantPermissionToGroup/enterprise", "Enterprise not found"));


        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Permission/grantPermissionToGroup/group", "Group not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Permission/grantPermissionToRole/role", "Role not found"));
        
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new NotFoundException("Permission/grantPermissionToRole/action", "Action not found"));

        Permission permission = new Permission();
        permission.setRole(role);
        permission.setAction(action);
        permission.setAllowed(true);
        permission.setEnterprise(enterprise);
        permission.setGroup(group);
        permissionRepository.save(permission);
    }

    @Override
    public void revokePermissionFromRole(UUID roleId, UUID actionId, UUID enterpriseId) {
        Permission permission = permissionRepository
                .findByRoleIdAndActionIdAndEnterpriseId(roleId, actionId, enterpriseId)
                .orElseThrow(() -> new NotFoundException("Permission/revokePermissionFromRole", "Permission not found"));
        permission.setAllowed(false);
        permissionRepository.save(permission);
    }

    @Override
    public void grantPermissionToGroup(UUID groupId, UUID actionId, UUID enterpriseId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Permission/grantPermissionToGroup/group", "Group not found"));
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new NotFoundException("Permission/grantPermissionToGroup/action", "Action not found"));

        if (!group.getEnterprise().getId().equals(enterpriseId)) {
            throw new NotFoundException("Permission/grantPermissionToGroup/equals", "Group not found in this enterprise");
        }

        Permission permission = new Permission();
        permission.setGroup(group);
        permission.setAction(action);
        permissionRepository.save(permission);
    }

    @Override
    public void revokePermissionFromGroup(UUID groupId, UUID actionId, UUID enterpriseId) {
        Permission permission = permissionRepository
                .findByGroupIdAndActionIdAndEnterpriseId(groupId, actionId, enterpriseId)
                .orElseThrow(() -> new NotFoundException("Permission/revokePermissionFromGroup/permission", "Permission not found"));

        if (!permission.getGroup().getEnterprise().getId().equals(enterpriseId)) {
            throw new NotFoundException("Permission/revokePermissionFromGroup/equals", "Permission not found in this enterprise");
        }

        permission.setDeleted(true);
        permissionRepository.save(permission);
    }

    @Override
    public boolean userHasPermissionForAction(UUID userId, UUID actionId, UUID enterpriseId) {
        return permissionRepository.userHasPermissionForAction(userId, actionId, enterpriseId);
    }
}