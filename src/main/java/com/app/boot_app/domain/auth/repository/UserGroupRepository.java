package com.app.boot_app.domain.auth.repository;

import com.app.boot_app.domain.auth.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {
    Optional<UserGroup> findByUserIdAndGroupId(UUID userId, UUID groupId);

    List<UserGroup> findByGroupId(UUID groupId);

    List<UserGroup> findByUserId(UUID userId);
}
