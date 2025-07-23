package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.CreateRecruiterRequestDTO;
import com.app.boot_app.domain.auth.dto.RecruiterResponseDTO;
import com.app.boot_app.domain.auth.entity.Recruiter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecruiterMapper {
    RecruiterMapper INSTANCE = Mappers.getMapper(RecruiterMapper.class);

    @Mapping(source = "userId", target = "user.id")
    Recruiter toEntity(CreateRecruiterRequestDTO dto);

    @Mapping(source = "user.id", target = "userId")
    RecruiterResponseDTO toDto(Recruiter entity);
}