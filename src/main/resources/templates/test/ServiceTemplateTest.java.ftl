
package ${packageName}.service;

import ${packageName}.dto.${className}Dto;
import ${packageName}.entity.${className}Entity;
import ${packageName}.repository.${className}Repository;
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.service.impl.${className}ServiceImpl
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ${className}ServiceTest {

    @Mock
    private ${className}Repository repository;

    @Mock
    private ${className}Mapper mapper;

    @InjectMocks
    private ${className}ServiceImpl service;

    private ${className}Entity entity;
    private ${className}Dto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new ${className}Entity();
        dto = new ${className}Dto("XXX");
    }

    @Test
    void testGetById_shouldReturnDto_whenEntityExists() {
        when(repository.findById("id")).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        ${className}Dto result = service.find("id");

        assertThat(result).isNotNull();
        verify(repository).findById("id");
    }
}
