package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.entity.UserGroup;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.domain.auth.repository.UserGroupRepository;
import com.app.boot_app.domain.auth.repository.UserRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    public void addUserToGroup(UUID userId, UUID groupId, UUID enterpriseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "User not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "Group not found"));

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);
    }

    @Override
    public void removeUserFromGroup(UUID userId, UUID groupId, UUID enterpriseId) {
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "User not found in this group"));
             
        userGroup.setDeleted(true);
        userGroupRepository.save(userGroup);
    }

    @Override
    public List<User> listUsersInGroup(UUID groupId) {
        return userGroupRepository.findByGroupId(groupId).stream()
                .map(UserGroup::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> listGroupsForUser(UUID userId) {
        return userGroupRepository.findByUserId(userId).stream()
                .map(UserGroup::getGroup)
                .collect(Collectors.toList());
    }
}
