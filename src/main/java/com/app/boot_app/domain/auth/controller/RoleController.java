package com.app.boot_app.domain.auth.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.boot_app.domain.auth.dto.RoleRequestDTO;
import com.app.boot_app.domain.auth.dto.RoleResponseDTO;
import com.app.boot_app.domain.auth.service.RoleService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;
    private final MessageSource messageSource;

    public RoleController(RoleService roleService, MessageSource messageSource) {
        this.roleService = roleService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ApiResponse<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        RoleResponseDTO result = roleService.createRole(roleRequestDTO);
        return Response.ok(messageSource.getMessage("role.created", null, LocaleContextHolder.getLocale()), result);
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponseDTO> getRoleById(@PathVariable UUID id) {
        RoleResponseDTO result = roleService.getRoleById(id);
        return Response.ok(messageSource.getMessage("role.found", null, LocaleContextHolder.getLocale()), result);
    }

    @GetMapping
    public ApiResponse<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> result = roleService.getAllRoles();
        return Response.ok(messageSource.getMessage("role.listed", null, LocaleContextHolder.getLocale()), result);
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleResponseDTO> updateRole(@PathVariable UUID id,
            @Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        RoleResponseDTO result = roleService.updateRole(id, roleRequestDTO);
        return Response.ok(messageSource.getMessage("role.updated", null, LocaleContextHolder.getLocale()), result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return Response.ok(messageSource.getMessage("role.deleted", null, LocaleContextHolder.getLocale()), null);
    }
}
