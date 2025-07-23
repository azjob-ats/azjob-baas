package com.app.boot_app.domain.auth.controller;

import com.app.boot_app.domain.auth.dto.ActionResponseDTO;
import com.app.boot_app.domain.auth.dto.CreateActionRequestDTO;
import com.app.boot_app.domain.auth.service.ActionService;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping
    public ApiResponse<List<ActionResponseDTO>> findAll() {
        return Response.ok("Actions retrieved successfully", actionService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<ActionResponseDTO> findById(@PathVariable UUID id) {
        return Response.ok("Action retrieved successfully", actionService.findById(id));
    }

    @PostMapping
    public ApiResponse<ActionResponseDTO> create(@Valid @RequestBody CreateActionRequestDTO dto) {
        return Response.created("Action created successfully", actionService.create(dto));
    }
}
