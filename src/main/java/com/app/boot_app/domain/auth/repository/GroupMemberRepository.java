package com.app.boot_app.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupMember;
import com.app.boot_app.domain.auth.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {
    Optional<GroupMember> findByGroupAndUser(Group group, User user);
    List<GroupMember> findByGroup(Group group);
}
