package com.app.boot_app.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupAuditLog;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupAuditLogRepository extends JpaRepository<GroupAuditLog, UUID> {
    List<GroupAuditLog> findByGroup(Group group);
}
