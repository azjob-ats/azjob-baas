
package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO for creating a new group")
public class CreateGroupRequestDTO {

    @NotBlank(message = "The group name is required.")
    @Schema(description = "The name of the group.", example = "Admins")
    private String name;
   
    @NotBlank(message = "The group description is required.")
    @Schema(description = "A brief description of the group.", example = "Group for administrators with full access")
    private String description;
    
    @NotNull(message = "The enterprise ID is required.")
    @Schema(description = "The unique identifier of the enterprise to which the group belongs.", example = "c1d2e3f4-g5h6-7890-1234-567890abcdef")
    private UUID enterpriseId;
}
