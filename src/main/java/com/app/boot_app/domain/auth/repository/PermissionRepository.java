package com.app.boot_app.domain.auth.repository;

import com.app.boot_app.domain.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByRoleIdAndActionId(UUID roleId, UUID actionId);

    Optional<Permission> findByGroupIdAndActionId(UUID groupId, UUID actionId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
           "FROM Permission p " +
           "WHERE p.action.name = :actionName " +
           "AND ( " +
           "    p.role.id IN (SELECT ur.role.id FROM UserRole ur WHERE ur.user.id = :userId) " +
           "    OR " +
           "    p.group.id IN (SELECT ug.group.id FROM UserGroup ug WHERE ug.user.id = :userId) " +
           ")")
    boolean existsByUserIdAndActionName(@Param("userId") UUID userId, @Param("actionName") String actionName);
}
