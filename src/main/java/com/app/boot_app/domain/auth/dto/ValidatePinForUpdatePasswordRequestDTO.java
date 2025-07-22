package com.app.boot_app.domain.auth.dto;

import lombok.Data;

@Data
public class ValidatePinForUpdatePasswordRequestDTO {
    private String email;
    private String code;
}
