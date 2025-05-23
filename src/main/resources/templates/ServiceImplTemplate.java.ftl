package ${packageName}.service.impl;

import org.springframework.stereotype.Service;

import ${packageName}.dto.${className}Dto;
import ${packageName}.service.${className}Service;
import ${packageName}.entity.${className}Entity;
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.repository.${className}Repository;

import lombok.AllArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ${className}ServiceImpl implements ${className}Service {

    private final ${className}Repository repository;
    private final ${className}Mapper mapper;

    @Override
    public ${className}Dto save(${className}Dto dto){
        ${className}Entity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ${className}Dto find(String id) {
        ${className}Entity entity = repository.findById(id)
            .orElseThrow();
        return mapper.toDto(entity);
    }

    @Override
    public List<${className}Dto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ${className}Dto update(String id, ${className}Dto dto) {
        ${className}Entity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found")); // A personnaliser

        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(String id) {
        ${className}Entity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found")); // A personnaliser
        repository.delete(entity);
    }
}
