package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.CreatePermissionRequestDTO;
import com.app.boot_app.domain.auth.dto.PermissionResponseDTO;
import com.app.boot_app.domain.auth.service.PermissionService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponseDTO>> findAll() {
        return Response.ok("Permissions retrieved successfully", permissionService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionResponseDTO> findById(@PathVariable UUID id) {
        return Response.ok("Permission retrieved successfully", permissionService.findById(id));
    }

    @PostMapping
    public ApiResponse<PermissionResponseDTO> create(@Valid @RequestBody CreatePermissionRequestDTO dto) {
        return Response.created("Permission created successfully", permissionService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        permissionService.delete(id);
        return Response.noContent();
    }
}
