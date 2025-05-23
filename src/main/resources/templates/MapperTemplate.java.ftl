package ${packageName}.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;

import ${packageName}.dto.${className}Dto;
import ${packageName}.entity.${className}Entity;

@Mapper(componentModel = "spring")
public interface ${className}Mapper {
    ${className}Mapper INSTANCE = Mappers.getMapper(${className}Mapper.class);
    ${className}Dto toDto(${className}Entity entity);
    ${className}Entity toEntity(${className}Dto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(${className}Dto dto, @MappingTarget ${className}Entity entity);
}
