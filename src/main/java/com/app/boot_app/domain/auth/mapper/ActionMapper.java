package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.ActionResponseDTO;
import com.app.boot_app.domain.auth.dto.CreateActionRequestDTO;
import com.app.boot_app.domain.auth.entity.Action;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActionMapper {
    ActionMapper INSTANCE = Mappers.getMapper(ActionMapper.class);

    Action toEntity(CreateActionRequestDTO dto);

    ActionResponseDTO toDto(Action entity);
}
