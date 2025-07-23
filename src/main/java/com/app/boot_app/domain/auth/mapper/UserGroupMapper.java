package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.CreateUserGroupRequestDTO;
import com.app.boot_app.domain.auth.dto.UserGroupResponseDTO;
import com.app.boot_app.domain.auth.entity.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserGroupMapper {
    UserGroupMapper INSTANCE = Mappers.getMapper(UserGroupMapper.class);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "groupId", target = "group.id")
    UserGroup toEntity(CreateUserGroupRequestDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "group.id", target = "groupId")
    UserGroupResponseDTO toDto(UserGroup entity);
}
