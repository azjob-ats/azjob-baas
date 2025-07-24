package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Response DTO for group information")
public class GroupResponseDTO {
    @Schema(description = "The unique identifier of the group.", example = "b1c2d3e4-f5g6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "The name of the group.", example = "Admins")
    private String name;
    @Schema(description = "A brief description of the group.", example = "Group for administrators with full access")
    private String description;
}
