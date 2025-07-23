package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.CreateEnterpriseRequestDTO;
import com.app.boot_app.domain.auth.dto.EnterpriseResponseDTO;
import com.app.boot_app.domain.auth.service.EnterpriseService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enterprises")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping
    public ApiResponse<List<EnterpriseResponseDTO>> findAll() {
        return Response.ok("Enterprises retrieved successfully", enterpriseService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<EnterpriseResponseDTO> findById(@PathVariable UUID id) {
        return Response.ok("Enterprise retrieved successfully", enterpriseService.findById(id));
    }

    @PostMapping
    public ApiResponse<EnterpriseResponseDTO> create(@Valid @RequestBody CreateEnterpriseRequestDTO dto) {
        return Response.created("Enterprise created successfully", enterpriseService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        enterpriseService.delete(id);
        return Response.noContent();
    }
}
