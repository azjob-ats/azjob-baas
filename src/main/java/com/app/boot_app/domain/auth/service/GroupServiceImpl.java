package com.app.boot_app.domain.auth.service;

import com.app.boot_app.domain.auth.entity.Enterprise;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.repository.EnterpriseRepository;
import com.app.boot_app.domain.auth.repository.GroupRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final EnterpriseRepository enterpriseRepository;

    @Override
    public Group createGroupForEnterprise(String groupName, String description, UUID enterpriseId) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "Enterprise not found"));

        Group group = new Group();
        group.setName(groupName);
        group.setDescription(description);
        group.setEnterprise(enterprise);
        group.setDeleted(false);

        return groupRepository.save(group);
    }

    @Override
    public List<Group> listGroupsByEnterprise(UUID enterpriseId) {
        return groupRepository.findByEnterpriseIdAndIsDeletedFalse(enterpriseId);
    }

    @Override
    public void deactivateGroup(UUID groupId, UUID enterpriseId) {
        Group group = groupRepository.findByIdAndEnterpriseId(groupId, enterpriseId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "Group not found in this enterprise"));

        group.setDeleted(true);
        groupRepository.save(group);
    }
}
