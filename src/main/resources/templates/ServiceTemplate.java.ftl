package ${packageName}.service;


import ${packageName}.dto.${className}Dto;

import java.util.List;

public interface ${className}Service {

    ${className}Dto save(${className}Dto dto);
    ${className}Dto find(String id);
    List<${className}Dto> findAll();
    ${className}Dto update(String id, ${className}Dto dto);
    void delete(String id);

}
