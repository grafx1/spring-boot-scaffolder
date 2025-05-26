package io.github.grafx1.scaffolder.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolsTest {

    @Test
    void testExtractPomInfoWithValidPom() {
        String pom = """
                    <project>
                      <modelVersion>4.0.0</modelVersion>
                      <groupId>com.example</groupId>
                      <artifactId>demo-app</artifactId>
                    </project>
                """;

        Map<String, String> info = Tools.extractPomInfo(pom);
        assertEquals("com.example", info.get("groupId"));
        assertEquals("demo-app", info.get("artifactId"));
    }

    @Test
    void testExtractPomInfoWithoutGroupAndArtifact() {
        String pom = "<project><version>1.0.0</version></project>";

        Map<String, String> info = Tools.extractPomInfo(pom);
        assertEquals("com.unknown", info.get("groupId"));
        assertEquals("app", info.get("artifactId"));
    }

    @Test
    void testPrintHelpPrintsToConsole() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Tools.printHelp();

        String output = out.toString();
        assertTrue(output.contains("Spring Boot Scaffolder Plugin"));
        System.setOut(System.out); // Reset
    }

    @Test
    void testPrintFooterWithTests() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Tools.printFooter("com.example.User", true, "/tmp/project");

        String output = out.toString();
        assertTrue(output.contains("With tests"));
        assertTrue(output.contains("Yes"));
        System.setOut(System.out);
    }

    @Test
    void testComputeFQCNFromProjectWithDefaultsWhenNoFiles() {
        Path fakeDir = Paths.get("non-existent-dir");
        String fqcn = Tools.computeFQCNFromProject(fakeDir, "User");
        System.out.println(fqcn);
        assertTrue(fqcn.endsWith(".User"));
    }

    @Test
    void testPrintHeader() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Tools.printHeader();

        String output = out.toString();
        assertTrue(output.contains("SPRING BOOT SCAFFOLDER PLUGIN"));
        System.setOut(System.out);
    }
}
