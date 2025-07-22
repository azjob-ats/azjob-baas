package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Request DTO for creating or updating a role")
public class RoleRequestDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    @Schema(description = "Name of the role (e.g., ADMIN, USER)", example = "ADMIN")
    private String name;
    @Size(max = 255)
    @Schema(description = "Description of the role's permissions", example = "Administrator role with full access")
    private String description;
    @NotNull
    @Schema(description = "List of permissions associated with this role", example = "[\"users:read\", \"users:write\"]")
    private List<String> permissions;
    @Schema(description = "Whether this role is assigned to new users by default", example = "false")
    private Boolean isDefault = false;
    @Schema(description = "Hierarchical level of the role (1-100)", example = "100")
    private Integer level = 1;
}
