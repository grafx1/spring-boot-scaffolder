package ${packageName}.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import ${packageName}.dto.${className}Dto;
import ${packageName}.service.${className}Service;

@RestController
@RequestMapping("/api/${classNameLower}s")
@AllArgsConstructor
public class ${className}Controller {

    private final ${className}Service service;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.find(id));
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(searchTerm, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody ${className}Dto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ${className}Dto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
