package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.CreateRoleRequestDTO;
import com.app.boot_app.domain.auth.dto.RoleResponseDTO;
import com.app.boot_app.domain.auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toEntity(CreateRoleRequestDTO dto);

    RoleResponseDTO toDto(Role entity);
}
