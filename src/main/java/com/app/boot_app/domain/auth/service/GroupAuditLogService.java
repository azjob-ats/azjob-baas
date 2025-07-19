package com.app.boot_app.domain.auth.service;

import java.util.List;
import java.util.UUID;

import com.app.boot_app.domain.auth.dto.GroupAuditLogResponseDTO;
import com.app.boot_app.domain.auth.entity.GroupAuditLog;

public interface GroupAuditLogService {
    List<GroupAuditLogResponseDTO> getAuditLogsByGroupId(UUID groupId);
    GroupAuditLogResponseDTO getAuditLogById(UUID id);
    GroupAuditLog createAuditLog(GroupAuditLog auditLog);
}