package sn.consultit.scaffolder;

import freemarker.template.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GeneratorService {

    private final Configuration cfg;

    public GeneratorService() {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);

        // Utilise les templates comme ressources du classpath
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
    }

    public void generateAll(String fqcn, Path baseDir) {
        String[] parts = fqcn.split("\\.");
        if (parts.length < 2) throw new IllegalArgumentException("Fully qualified class name expected (e.g. user.User)");

        String packageName = String.join(".", Arrays.copyOf(parts, parts.length - 1));
        String className = parts[parts.length - 1];
        String classNameLower = className.toLowerCase();

        Map<String, Object> data = new HashMap<>();
        data.put("packageName", packageName);
        data.put("className", className);
        data.put("classNameLower", classNameLower);

        String[] types = {"Entity", "Dto", "Repository", "Service", "ServiceImpl", "Controller", "Mapper"};
        String[] subPackages = {"entity", "dto", "repository", "service", "service/impl", "controller", "mapper"};

        for (int i = 0; i < types.length; i++) {
            Path outputDir = baseDir.resolve("src/main/java")
                    .resolve(packageName.replace('.', '/'))
                    .resolve(subPackages[i]);
            generateFile(types[i] + "Template.java.ftl", outputDir, className + types[i] + ".java", data);
        }
    }

    private void generateFile(String templateName, Path outputDir, String outputFile, Map<String, Object> data) {
        try {
            Template template = cfg.getTemplate(templateName);
            Files.createDirectories(outputDir); // Assure la création du répertoire
            try (Writer writer = new FileWriter(outputDir.resolve(outputFile).toFile())) {
                template.process(data, writer);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate file: " + outputFile, e);
        }
    }

}
