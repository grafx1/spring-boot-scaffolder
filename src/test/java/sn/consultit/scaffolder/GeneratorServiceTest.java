package sn.consultit.scaffolder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.tools.ant.Project.getProject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GeneratorServiceTest {

    private GeneratorService generatorService;

    @BeforeEach
    void setUp() {
        generatorService = new GeneratorService();
    }

    @Test
    void testGenerateAll_shouldCreateAllExpectedFiles(@TempDir Path tempDir) throws IOException {
        // GIVEN
        String fqcn = "com.example.test.User";

        // WHEN
        generatorService.generateAll(fqcn, tempDir, true);

        // THEN
        List<String> expectedFiles = List.of(
                "entity/UserEntity.java",
                "dto/UserDto.java",
                "repository/UserRepository.java",
                "service/UserService.java",
                "service/impl/UserServiceImpl.java",
                "controller/UserController.java",
                "mapper/UserMapper.java"
        );

        for (String relPath : expectedFiles) {
            Path filePath = tempDir.resolve("src/main/java/com/example/test").resolve(relPath);
            assertThat(Files.exists(filePath))
                    .withFailMessage("Expected file does not exist: " + filePath)
                    .isTrue();

            // Vérifie que le fichier contient au moins le nom de la classe
            String content = Files.readString(filePath);
            assertThat(content).contains("User");
        }
    }
}
