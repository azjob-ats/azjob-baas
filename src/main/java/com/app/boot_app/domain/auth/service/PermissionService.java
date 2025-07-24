package com.app.boot_app.domain.auth.service;

import java.util.UUID;

public interface PermissionService {
    void grantPermissionToRole(UUID roleId, UUID actionId, UUID enterpriseId);

    void revokePermissionFromRole(UUID roleId, UUID actionId, UUID enterpriseId);

    void grantPermissionToGroup(UUID groupId, UUID actionId, UUID enterpriseId);

    void revokePermissionFromGroup(UUID groupId, UUID actionId, UUID enterpriseId);

    boolean userHasPermissionForAction(UUID userId, UUID actionId, UUID enterpriseId);
}
