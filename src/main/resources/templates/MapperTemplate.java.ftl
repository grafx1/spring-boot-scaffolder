package ${packageName}.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import ${packageName}.dto.${className}Dto;
import ${packageName}.entity.${className}Entity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ${className}Mapper {
    ${className}Mapper INSTANCE = Mappers.getMapper(${className}Mapper.class);


    ${className}Dto toDto(${className}Entity entity);
    @Mapping(target = "version", ignore = true)
    ${className}Entity toEntity(${className}Dto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(${className}Dto dto, @MappingTarget ${className}Entity entity);

    List<${className}Dto> toDtoList(List<${className}Entity> entities);

    default Page<${className}Dto> toDtoPage(Page<${className}Entity> entities) {
        List<${className}Dto> dtoList = toDtoList(entities.getContent());
            return new PageImpl<>(dtoList, entities.getPageable(), entities.getTotalElements());
    }
}
