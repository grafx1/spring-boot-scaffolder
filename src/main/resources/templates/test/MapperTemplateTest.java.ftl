package ${packageName}.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import ${packageName}.dto.${className}Dto;
import ${packageName}.entity.${className}Entity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ${className}MapperTest {

    private final ${className}Mapper mapper = Mappers.getMapper(${className}Mapper.class);

    @Test
    void shouldMapEntityToDto() {
        ${className}Entity entity = buildEntity();
        ${className}Dto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        // TODO: add more assertions for fields
    }

    @Test
    void shouldMapDtoToEntity() {
        ${className}Dto dto = buildDto();
        ${className}Entity entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.id());
        // TODO: add more assertions
    }

    @Test
    void shouldUpdateEntityFromDto() {
        ${className}Entity target = new ${className}Entity();
        ${className}Dto source = buildDto();

        mapper.updateEntityFromDto(source, target);

        assertThat(target.getId()).isNull(); // id should be ignored
        // TODO: assert updated fields
    }

    @Test
    void shouldMapEntityListToDtoList() {
        List<${className}Entity> entities = List.of(buildEntity(), buildEntity());
        List<${className}Dto> dtos = mapper.toDtoList(entities);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.getFirst()).isNotNull();
    }

    // Sample mock data
    private ${className}Entity buildEntity() {
        return ${className}Entity.builder()
            .id("XXX")
            .reference("Refs")
            .build(); // TODO: initialize other fields

    }

    private ${className}Dto buildDto() {
        return new ${className}Dto("XXX", "ref"); // TODO: initialize other fields
    }

}
