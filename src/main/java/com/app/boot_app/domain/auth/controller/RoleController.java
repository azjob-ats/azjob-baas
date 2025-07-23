package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.CreateRoleRequestDTO;
import com.app.boot_app.domain.auth.dto.RoleResponseDTO;
import com.app.boot_app.domain.auth.service.RoleService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ApiResponse<List<RoleResponseDTO>> findAll() {
        return Response.ok("Roles retrieved successfully", roleService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponseDTO> findById(@PathVariable UUID id) {
        return Response.ok("Role retrieved successfully", roleService.findById(id));
    }

    @PostMapping
    public ApiResponse<RoleResponseDTO> create(@Valid @RequestBody CreateRoleRequestDTO dto) {
        return Response.created("Role created successfully", roleService.create(dto));
    }
}
