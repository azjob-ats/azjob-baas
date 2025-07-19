package com.app.boot_app.domain.auth.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.boot_app.domain.auth.dto.GroupAuditLogResponseDTO;
import com.app.boot_app.domain.auth.service.GroupAuditLogService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;

@RestController
public class GroupAuditLogController {

    private final GroupAuditLogService auditLogService;
    private final MessageSource messageSource;

    public GroupAuditLogController(GroupAuditLogService auditLogService, MessageSource messageSource) {
        this.auditLogService = auditLogService;
        this.messageSource = messageSource;
    }

    @GetMapping("/api/v1/audit-logs/{id}")
    public ApiResponse<GroupAuditLogResponseDTO> getAuditLogById(@PathVariable UUID id) {
        GroupAuditLogResponseDTO result = auditLogService.getAuditLogById(id);
        return Response.ok(messageSource.getMessage("audit.log.found", null, LocaleContextHolder.getLocale()), result);
    }

    @GetMapping("/api/v1/groups/{id}/audit-logs")
    public ApiResponse<List<GroupAuditLogResponseDTO>> getAuditLogsByGroupId(@PathVariable UUID id) {
        List<GroupAuditLogResponseDTO> result = auditLogService.getAuditLogsByGroupId(id);
        return Response.ok(messageSource.getMessage("audit.logs.listed", null, LocaleContextHolder.getLocale()),
                result);
    }
}
