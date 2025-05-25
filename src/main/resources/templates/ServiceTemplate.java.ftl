package ${packageName}.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ${packageName}.dto.${className}Dto;

public interface ${className}Service {

    ${className}Dto save(${className}Dto dto);
    ${className}Dto find(String id);
    Page<${className}Dto> findAll(String term, Pageable pageable);
    ${className}Dto update(String id, ${className}Dto dto);
    void delete(String id);

}
