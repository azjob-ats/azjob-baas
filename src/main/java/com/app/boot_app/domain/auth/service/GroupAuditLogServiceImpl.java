package com.app.boot_app.domain.auth.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.dto.GroupAuditLogResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;
import com.app.boot_app.domain.auth.entity.GroupAuditLog;
import com.app.boot_app.domain.auth.exception.NotFoundException;
import com.app.boot_app.domain.auth.repository.GroupAuditLogRepository;
import com.app.boot_app.domain.auth.repository.GroupRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupAuditLogServiceImpl implements GroupAuditLogService {

    private final GroupAuditLogRepository auditLogRepository;
    private final GroupRepository groupRepository;
    private final MessageSource messageSource;

    public GroupAuditLogServiceImpl(GroupAuditLogRepository auditLogRepository, GroupRepository groupRepository, MessageSource messageSource) {
        this.auditLogRepository = auditLogRepository;
        this.groupRepository = groupRepository;
        this.messageSource = messageSource;
    }

    @Override
    public List<GroupAuditLogResponseDTO> getAuditLogsByGroupId(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                                .orElseThrow(() -> new NotFoundException("group-not-found", messageSource.getMessage("group.not.found", null, LocaleContextHolder.getLocale())));
        return auditLogRepository.findByGroup(group).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupAuditLogResponseDTO getAuditLogById(UUID id) {
        GroupAuditLog auditLog = auditLogRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("audit-log-not-found", messageSource.getMessage("audit.log.not.found", null, LocaleContextHolder.getLocale())));
        return convertToDto(auditLog);
    }

    @Override
    public GroupAuditLog createAuditLog(GroupAuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    private GroupAuditLogResponseDTO convertToDto(GroupAuditLog auditLog) {
        GroupAuditLogResponseDTO dto = new GroupAuditLogResponseDTO();
        dto.setId(auditLog.getId());
        dto.setGroupId(auditLog.getGroup().getId());
        dto.setAction(auditLog.getAction());
        dto.setPerformedBy(auditLog.getPerformedBy().getId());
        dto.setPerformedAt(auditLog.getPerformedAt());
        if (auditLog.getTargetUser() != null) {
            dto.setTargetUserId(auditLog.getTargetUser().getId());
        }
        dto.setOldValue(auditLog.getOldValue());
        dto.setNewValue(auditLog.getNewValue());
        dto.setIpAddress(auditLog.getIpAddress());
        return dto;
    }
}
