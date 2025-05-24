
package ${packageName}.controller;

import ${packageName}.dto.${className}Dto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ${className}ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAll_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/${classNameLower}s"))
            .andExpect(status().isOk());
    }

    @Test
    void testCreate_shouldReturnCreated() throws Exception {
        ${className}Dto dto = new ${className}Dto("XXX"); // Remplir les champs si besoin

        mockMvc.perform(post("/api/${classNameLower}s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }
}
