package ${packageName}.mapper;

import ${packageName}.dto.${className}Dto;
import ${packageName}.entity.${className}Entity;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;

@Mapper
public interface ${className}Mapper {
    ${className}Mapper INSTANCE = Mappers.getMapper(${className}Mapper.class);
    ${className}Dto toDto(${className}Entity entity);
    ${className}Entity toEntity(${className}Dto dto);
}
