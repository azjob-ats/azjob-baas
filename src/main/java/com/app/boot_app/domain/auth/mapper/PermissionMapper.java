package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.CreatePermissionRequestDTO;
import com.app.boot_app.domain.auth.dto.PermissionResponseDTO;
import com.app.boot_app.domain.auth.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    @Mapping(source = "idRole", target = "role.id")
    @Mapping(source = "idAction", target = "action.id")
    @Mapping(source = "idGroup", target = "group.id")
    Permission toEntity(CreatePermissionRequestDTO dto);

    @Mapping(source = "role.id", target = "idRole")
    @Mapping(source = "action.id", target = "idAction")
    @Mapping(source = "group.id", target = "idGroup")
    PermissionResponseDTO toDto(Permission entity);
}
