package ${packageName}.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ${packageName}.entity.${className}Entity;

public interface ${className}Repository extends JpaRepository<${className}Entity, String> {

    @Query("SELECT e FROM ${className}Entity e  " +
        "WHERE LOWER(e.reference) LIKE LOWER(CONCAT('%', :term, '%')) ")
    Page<${className}Entity> findAll(@Param("term") String term, Pageable pageable);
}