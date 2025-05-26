package ${packageName}.controller;

import ${basePackage}.dto.PagedResponse;
import ${packageName}.dto.${className}Dto;
import ${packageName}.service.${className}Service;


import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(${className}Controller.class)
@Import(${className}ControllerTest.TestConfig.class)
class ${className}ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ${className}Service service;

    @Autowired
    private ObjectMapper objectMapper;



    @TestConfiguration
    static class TestConfig {
        @Bean
        public ${className}Service ${classNameLower}Service() {
            return Mockito.mock(${className}Service.class);
        }
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
    }

    @Test
    void shouldReturnDtoOnFindById() throws Exception {
        ${className}Dto dto = buildDto();
        when(service.find("XX")).thenReturn(dto);

        mockMvc.perform(get("/api/${classNameLower}s/XX"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("XX"));
    }

    @Test
    void shouldSaveDto() throws Exception {
        ${className}Dto dto = buildDto();
        when(service.save(any(${className}Dto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/${classNameLower}s")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("XX"));
    }

    @Test
    void shouldUpdateDto() throws Exception {
        ${className}Dto dto = buildDto();
        when(service.update(eq("XX"), any(${className}Dto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/${classNameLower}s/XX")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("XX"));
    }

    @Test
    void shouldDeleteById() throws Exception {
        mockMvc.perform(delete("/api/${classNameLower}s/XX"))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnPagedList() throws Exception {
    Page<${className}Dto> page = new PageImpl<>(List.of(buildDto()));
        PagedResponse<${className}Dto> response = PagedResponse.from(page);

        when(service.findAll(anyString(), any())).thenReturn(response);

        mockMvc.perform(get("/api/${classNameLower}s")
        .param("term", "test")
        .param("page", "0")
        .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.totalElements").value(1));
    }

    private ${className}Dto buildDto() {
        return new ${className}Dto("XX", "ref"); // TODO: initialize other fields
    }
}
