package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.UserResponseDTO;
import com.app.boot_app.domain.auth.entity.User;
import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /*
        @Mapping(source = "role.id", target = "roleId")
        role.id deve entity User
        roleId deve esta no dto UserResponseDTO
        isso quando role é uma entidade
    */
    UserResponseDTO toResponse(User user);
}