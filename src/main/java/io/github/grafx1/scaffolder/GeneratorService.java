package io.github.grafx1.scaffolder;

import freemarker.template.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static io.github.grafx1.scaffolder.utils.Tools.*;

public class GeneratorService {

    private final Configuration cfgMain;
    private final Configuration cfgTest;

    public GeneratorService() {
        cfgMain = new Configuration(Configuration.VERSION_2_3_32);
        cfgMain.setDefaultEncoding("UTF-8");
        cfgMain.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfgMain.setLogTemplateExceptions(false);
        cfgMain.setWrapUncheckedExceptions(true);
        cfgMain.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");

        cfgTest = new Configuration(Configuration.VERSION_2_3_32);
        cfgTest.setDefaultEncoding("UTF-8");
        cfgTest.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfgTest.setLogTemplateExceptions(false);
        cfgTest.setWrapUncheckedExceptions(true);
        cfgTest.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates/test");
    }

    public void generateStructure(String entityNames, Path baseDir, boolean withTests) {
        Arrays.stream(entityNames.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(entity -> {
                    if (entity.split("\\.").length > 1) {
                        printHelp();
                        throw new IllegalArgumentException("Invalid entity name : " + entity);
                    }
                    String fqcn = computeFQCNFromProject(baseDir, entity);
                    printFooter(fqcn, withTests, baseDir.toString());

                    generateAll(fqcn, baseDir, withTests);
                });


        System.out.println(GREEN + "\n Scaffold generated successfully!" + RESET);
    }

    public void generateAll(String fqcn, Path baseDir, boolean withTests) {
        String[] parts = fqcn.split("\\.");
        String className = parts[parts.length - 1];
        String classNameLower = className.toLowerCase();
        String basePackage = String.join(".", Arrays.copyOf(parts, parts.length - 1));

        Map<String, Object> data = new HashMap<>();
        data.put("className", className);
        data.put("classNameLower", classNameLower);

        String[] types = {"Entity", "Dto", "Repository", "Service", "ServiceImpl", "Controller", "Mapper"};
        String[] subPackages = {"entity", "dto", "repository", "service", "service.impl", "controller", "mapper"};
        String[] testTypes = {"Mapper", "Controller", "Service"};

        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            String subPackage = basePackage + "." + subPackages[i];

            Map<String, Object> dataCopy = new HashMap<>(data);
            dataCopy.put("packageName", basePackage);

            // Génération du code principal
            String outputDir = baseDir + "/src/main/java/" + subPackage.replace('.', '/');
            String outputFile = className + type + ".java";
            generateFile(cfgMain, type + "Template.java.ftl", outputDir, outputFile, dataCopy);

            // Génération du test unitaire
            if (withTests && Arrays.asList(testTypes).contains(type)) {
                String testOutputDir = baseDir + "/src/test/java/" + subPackage.replace('.', '/');
                String testOutputFile = className + type + "Test.java";
                String testTemplate = type + "TemplateTest.java.ftl";
                generateFile(cfgTest, testTemplate, testOutputDir, testOutputFile, dataCopy);
            }
        }
    }

    private void generateFile(Configuration cfg, String templateName, String outputDir, String outputFile, Map<String, Object> data) {
        try {
            Template template = cfg.getTemplate(templateName);
            Path dirPath = Paths.get(outputDir);
            Files.createDirectories(dirPath);
            try (Writer writer = new FileWriter(dirPath.resolve(outputFile).toFile())) {
                template.process(data, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate file: " + outputFile, e);
        }
    }

}
