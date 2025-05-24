
package ${packageName}.mapper;

import ${packageName}.dto.${className}Dto;
import ${packageName}.entity.${className}Entity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class ${className}MapperTest {

    private final ${className}Mapper mapper = Mappers.getMapper(${className}Mapper.class);

    @Test
    void testEntityToDtoAndBack() {
        ${className}Entity entity = new ${className}Entity();
        entity.setId("123");

        ${className}Dto dto = mapper.toDto(entity);
        ${className}Entity convertedBack = mapper.toEntity(dto);

        assertThat(dto).isNotNull();
        assertThat(convertedBack).isNotNull();
    }
}
