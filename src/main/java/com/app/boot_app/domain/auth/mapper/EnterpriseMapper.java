package com.app.boot_app.domain.auth.mapper;

import com.app.boot_app.domain.auth.dto.CreateEnterpriseRequestDTO;
import com.app.boot_app.domain.auth.dto.EnterpriseResponseDTO;
import com.app.boot_app.domain.auth.entity.Enterprise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EnterpriseMapper {
    EnterpriseMapper INSTANCE = Mappers.getMapper(EnterpriseMapper.class);

    @Mapping(source = "ownerId", target = "owner.id")
    Enterprise toEntity(CreateEnterpriseRequestDTO dto);

    @Mapping(source = "owner.id", target = "ownerId")
    EnterpriseResponseDTO toDto(Enterprise entity);
}
