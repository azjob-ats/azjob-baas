package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.CreateRecruiterRequestDTO;
import com.app.boot_app.domain.auth.dto.RecruiterResponseDTO;
import com.app.boot_app.domain.auth.service.RecruiterService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping
    public ApiResponse<List<RecruiterResponseDTO>> findAll() {
        return Response.ok("Recruiters retrieved successfully", recruiterService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<RecruiterResponseDTO> findById(@PathVariable UUID id) {
        return Response.ok("Recruiter retrieved successfully", recruiterService.findById(id));
    }

    @PostMapping
    public ApiResponse<RecruiterResponseDTO> create(@Valid @RequestBody CreateRecruiterRequestDTO dto) {
        return Response.created("Recruiter created successfully", recruiterService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        recruiterService.delete(id);
        return Response.noContent();
    }
}
