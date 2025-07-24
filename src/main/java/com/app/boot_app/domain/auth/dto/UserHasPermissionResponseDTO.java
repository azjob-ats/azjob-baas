package com.app.boot_app.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserHasPermissionResponseDTO {
    private boolean hasPermission;
}
