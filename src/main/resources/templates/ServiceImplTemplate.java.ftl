package ${packageName}.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import ${packageName}.dto.${className}Dto;
import ${packageName}.service.${className}Service;
import ${packageName}.entity.${className}Entity;
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.repository.${className}Repository;

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
    public Page<${className}Dto> findAll(String term, Pageable pageable) {
        return mapper.toDtoPage(repository.findAll(term, pageable));
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
