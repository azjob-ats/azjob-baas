
package com.app.boot_app.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request DTO for creating a new group")
public class CreateGroupRequestDTO {

    @NotBlank(message = "O name é obrigatório")
    @Schema(description = "The name of the group.", example = "Admins")
    private String name;
   
    @NotBlank(message = "O description é obrigatório")
    @Schema(description = "A brief description of the group.", example = "Group for administrators with full access")
    private String description;
    
    @NotBlank(message = "O enterpriseId é obrigatório")
    @Schema(description = "The unique identifier of the enterprise to which the group belongs.", example = "c1d2e3f4-g5h6-7890-1234-567890abcdef")
    private UUID enterpriseId;
}
