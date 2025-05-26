package io.github.grafx1.scaffolder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorServiceTest {

    private GeneratorService generatorService;
    private Path testBaseDir;

    @BeforeEach
    void setUp() {
        generatorService = new GeneratorService();
        testBaseDir = Paths.get("build/test-output");
    }


    @Test
    void testGenerateStructureWithValidEntity() {
        assertDoesNotThrow(() -> generatorService.generateStructure("User", testBaseDir, false));
    }

    @Test
    void testGenerateStructureWithMultipleValidEntities() {
        assertDoesNotThrow(() -> generatorService.generateStructure("User,Product,Order", testBaseDir, true));
    }

    @Test
    void testGenerateStructureWithInvalidEntityNameThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                generatorService.generateStructure("com.example.User", testBaseDir, false));
        assertEquals("Invalid entity name : com.example.User", exception.getMessage());
    }

    @Test
    void testGenerateAllCreatesExpectedFiles() {
        generatorService.generateAll("com.example.demo.User", testBaseDir, true);

        Path expectedEntityPath = testBaseDir.resolve("src/main/java/com/example/demo/entity/UserEntity.java");
        assertTrue(expectedEntityPath.toFile().exists(), "Entity file should be generated");

        Path expectedTestPath = testBaseDir.resolve("src/test/java/com/example/demo/mapper/UserMapperTest.java");
        assertTrue(expectedTestPath.toFile().exists(), "Mapper test file should be generated");
    }

    @Test
    void testPagedResponseGeneratedOnce() {
        Path pagedResponsePath = testBaseDir.resolve("src/main/java/com/example/dto/PagedResponse.java");
        if (pagedResponsePath.toFile().exists()) {
            pagedResponsePath.toFile().delete();
        }

        generatorService.generateAll("com.example.demo.Customer", testBaseDir, false);
        assertTrue(pagedResponsePath.toFile().exists(), "PagedResponse.java should be generated");

        long lastModified = pagedResponsePath.toFile().lastModified();
        generatorService.generateAll("com.example.demo.Client", testBaseDir, false);
        assertEquals(lastModified, pagedResponsePath.toFile().lastModified(), "PagedResponse.java should not be regenerated");
    }
}
