package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.GroupResponseDTO;
import com.app.boot_app.domain.auth.entity.Group;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupResponseDTO groupToGroupResponseDTO(Group group);

    List<GroupResponseDTO> groupsToGroupResponseDTOs(List<Group> groups);
}
