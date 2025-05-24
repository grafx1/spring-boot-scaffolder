package io.github.grafx1.scaffolder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.*;
import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratorServiceTest {

    private GeneratorService generatorService;

    @BeforeEach
    void setUp() {
        generatorService = new GeneratorService();
    }

    @Test
    void testGenerateAll_shouldCreateAllExpectedFiles(@TempDir Path tempDir) throws IOException {
        // GIVEN
        String fqcn = "com.example.test.user.User";

        // WHEN
        generatorService.generateAll(fqcn, tempDir, true);

        // THEN
        List<String> expectedFiles = List.of(
                "user/entity/UserEntity.java",
                "user/dto/UserDto.java",
                "user/repository/UserRepository.java",
                "user/service/UserService.java",
                "user/service/impl/UserServiceImpl.java",
                "user/controller/UserController.java",
                "user/mapper/UserMapper.java"
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
