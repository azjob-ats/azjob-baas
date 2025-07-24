package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.entity.Group;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    Group createGroupForEnterprise(String groupName, String description, UUID enterpriseId);

    List<Group> listGroupsByEnterprise(UUID enterpriseId);

    void deactivateGroup(UUID groupId, UUID enterpriseId);
}
