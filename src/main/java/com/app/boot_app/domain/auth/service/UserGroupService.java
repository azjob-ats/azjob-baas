package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserGroupService {
    void addUserToGroup(UUID userId, UUID groupId, UUID enterpriseId);

    void removeUserFromGroup(UUID userId, UUID groupId, UUID enterpriseId);

    List<User> listUsersInGroup(UUID groupId);

    List<Group> listGroupsForUser(UUID userId);
}
