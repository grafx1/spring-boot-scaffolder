package io.github.grafx1.scaffolder;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Path;
import java.util.Arrays;

import static io.github.grafx1.scaffolder.utils.Tools.*;

public class ScaffoldTask extends DefaultTask {

    @TaskAction
    public void generate() {
        printHeader();
        String help = (String) getProject().findProperty("sHelp");
        if ("true".equalsIgnoreCase(help)) {
            printHelp();
            return;
        }

        String entityNames = (String) getProject().findProperty("entityName");
        boolean withTests = "true".equalsIgnoreCase((String) getProject().findProperty("withTests"));

        if (entityNames == null || entityNames.isBlank()) {
            System.err.println("Argument manquant : utilisez -PentityName=MyClass");
            printHelp();
            throw new IllegalArgumentException("Aucune entité n'a été fournie.");
        }

        Path projectRoot = getProject().getProjectDir().toPath();
        GeneratorService generatorService = new GeneratorService();

        generatorService.generateStructure(entityNames, projectRoot, withTests);

    }



}
