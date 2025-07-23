package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.CreateGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    Group toEntity(CreateGroupRequestDTO dto);

    GroupResponseDTO toDto(Group entity);
}
