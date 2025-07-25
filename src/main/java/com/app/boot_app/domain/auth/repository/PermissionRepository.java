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
    Optional<Permission> findByRoleIdAndActionIdAndIdEnterprise(UUID roleId, UUID actionId, UUID enterpriseId);

    Optional<Permission> findByGroupIdAndActionIdAndIdEnterprise(UUID groupId, UUID actionId, UUID enterpriseId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
           "FROM Permission p " +
           "WHERE p.action.id = :actionId " +
           "AND p.enterprise_id = :enterpriseId " +
           "AND p.allowed = TRUE " +
           "AND p.group.id IN (SELECT ug.group.id FROM UserGroup ug WHERE ug.user.id = :userId)")
    boolean userHasPermissionForAction(
        @Param("userId") UUID userId,
        @Param("actionId") UUID actionId,
        @Param("enterpriseId") UUID enterpriseId
    );
}
