package ${packageName}.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ${packageName}.dto.${className}Dto;
import ${packageName}.entity.${className}Entity;
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.repository.${className}Repository;
import ${packageName}.service.impl.${className}ServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ${className}ServiceTest {

    @Mock
    private ${className}Repository repository;

    @Mock
    private ${className}Mapper mapper;

    @InjectMocks
    private ${className}ServiceImpl service;

    @BeforeEach
    void setUp() {
    MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveDto() {
        ${className}Dto dto = buildDto();
        ${className}Entity entity = buildEntity();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ${className}Dto result = service.save(dto);

        assertThat(result).isNotNull();
        verify(repository).save(entity);
        verify(mapper).toDto(entity);
    }

    @Test
    void shouldFindById() {
        String id = "abc123";
        ${className}Entity entity = buildEntity();
        ${className}Dto dto = buildDto();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        ${className}Dto result = service.find(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.find("x")); // TODO update with your custom exception
    }

    @Test
    void shouldReturnPagedDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        List<${className}Entity> entities = List.of(buildEntity());
        Page<${className}Entity> page = new PageImpl<>(entities, pageable, 1);
        Page<${className}Dto> mappedPage = new PageImpl<>(List.of(buildDto()), pageable, 1);

        when(repository.findAll(anyString(), eq(pageable))).thenReturn(page);
        when(mapper.toDtoPage(page)).thenReturn(mappedPage);

        Page<${className}Dto> result = service.findAll("test", pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldUpdateExistingEntity() {
        String id = "id123";
        ${className}Dto dto = buildDto();
        ${className}Entity entity = buildEntity();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(mapper).updateEntityFromDto(dto, entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ${className}Dto result = service.update(id, dto);

        assertThat(result).isEqualTo(dto);
        verify(mapper).updateEntityFromDto(dto, entity);
    }

    @Test
    void shouldDeleteEntityById() {
        String id = "toDelete";
        ${className}Entity entity = buildEntity();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(repository).delete(entity);

        service.delete(id);

        verify(repository).delete(entity);
    }

    // Mock builders
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
