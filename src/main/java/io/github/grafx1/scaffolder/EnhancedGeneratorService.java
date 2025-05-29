package io.github.grafx1.scaffolder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.github.grafx1.scaffolder.utils.Tools.*;

/**
 * Service responsable de la génération de code pour les différents composants
 * d'une application Spring Boot. Cette version améliorée permet la génération
 * sélective de composants individuels et prend en compte un dossier de sortie personnalisé.
 */
public class EnhancedGeneratorService {

    // Types de composants supportés
    public static final String ENTITY = "Entity";
    public static final String DTO = "Dto";
    public static final String REPOSITORY = "Repository";
    public static final String SERVICE = "Service";
    public static final String SERVICE_IMPL = "ServiceImpl";
    public static final String CONTROLLER = "Controller";
    public static final String MAPPER = "Mapper";

    // Structure des sous-packages pour chaque type de composant
    private static final Map<String, String> SUBPACKAGES = Map.of(
            ENTITY, "entity",
            DTO, "dto",
            REPOSITORY, "repository",
            SERVICE, "service",
            SERVICE_IMPL, "service.impl",
            CONTROLLER, "controller",
            MAPPER, "mapper"
    );

    // Configuration pour les templates
    private final Configuration cfgMain;
    private final Configuration cfgTest;

    /**
     * Initialise le service de génération avec les configurations FreeMarker
     */
    public EnhancedGeneratorService() {
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

    /**
     * Génère la structure complète (tous les composants) pour les entités spécifiées
     *
     * @param entityNames Les noms des entités séparés par des virgules
     * @param baseDir Le répertoire de base du projet
     * @param withTests Indique si les tests unitaires doivent être générés
     */
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
                    System.out.println("fqcn : " + fqcn);

                    // Utilisation du dossier de base adapté pour les tests
                    Path effectiveBaseDir = getEffectiveBaseDir(baseDir);
                    printFooter(fqcn, withTests, effectiveBaseDir.toString());

                    generateAllComponents(fqcn, effectiveBaseDir, withTests);
                });

        System.out.println(GREEN + "\n Scaffold generated successfully!" + RESET);
    }

    /**
     * Génère tous les composants pour une entité spécifique
     */
    public void generateAllComponents(String fqcn, Path baseDir, boolean withTests) {
        // Obtenir le dossier de base effectif (pour les tests)
        Path effectiveBaseDir = getEffectiveBaseDir(baseDir);

        // Générer la classe PagedResponse si nécessaire
        generatePagedResponse(getBaseAppPackage(fqcn), effectiveBaseDir);

        // Générer tous les composants
        generateEntityComponent(fqcn, effectiveBaseDir, withTests);
        generateDtoComponent(fqcn, effectiveBaseDir, withTests);
        generateRepositoryComponent(fqcn, effectiveBaseDir, withTests);
        generateServiceComponent(fqcn, effectiveBaseDir, withTests);
        generateServiceImplComponent(fqcn, effectiveBaseDir, withTests);
        generateControllerComponent(fqcn, effectiveBaseDir, withTests);
        generateMapperComponent(fqcn, effectiveBaseDir, withTests);
    }

    /**
     * Détermine le dossier de base effectif en prenant en compte la propriété système scaffolder.output.dir
     * Cette méthode est utilisée pour rediriger la génération vers un dossier spécifique pendant les tests
     */
    private Path getEffectiveBaseDir(Path baseDir) {
        String outputDir = System.getProperty("scaffolder.output.dir");
        if (outputDir != null && !outputDir.isEmpty()) {
            System.out.println("Using output directory from system property: " + outputDir);
            return Paths.get(outputDir);
        }
        return baseDir;
    }

    /**
     * Génère uniquement le composant Entity
     */
    public void generateEntityComponent(String fqcn, Path baseDir, boolean withTests) {
        generateComponent(fqcn, getEffectiveBaseDir(baseDir), ENTITY, withTests);
    }

    /**
     * Génère uniquement le composant DTO
     */
    public void generateDtoComponent(String fqcn, Path baseDir, boolean withTests) {
        generateComponent(fqcn, getEffectiveBaseDir(baseDir), DTO, withTests);
    }

    /**
     * Génère uniquement le composant Repository
     */
    public void generateRepositoryComponent(String fqcn, Path baseDir, boolean withTests) {
        generateComponent(fqcn, getEffectiveBaseDir(baseDir), REPOSITORY, withTests);
    }

    /**
     * Génère uniquement le composant Service (interface)
     */
    public void generateServiceComponent(String fqcn, Path baseDir, boolean withTests) {
        generateComponent(fqcn, getEffectiveBaseDir(baseDir), SERVICE, withTests);
    }

    /**
     * Génère uniquement le composant ServiceImpl
     */
    public void generateServiceImplComponent(String fqcn, Path baseDir, boolean withTests) {
        generateComponent(fqcn, getEffectiveBaseDir(baseDir), SERVICE_IMPL, withTests);
    }

    /**
     * Génère uniquement le composant Controller
     */
    public void generateControllerComponent(String fqcn, Path baseDir, boolean withTests) {
        generateComponent(fqcn, getEffectiveBaseDir(baseDir), CONTROLLER, withTests);
    }

    /**
     * Génère uniquement le composant Mapper
     */
    public void generateMapperComponent(String fqcn, Path baseDir, boolean withTests) {
        generateComponent(fqcn, getEffectiveBaseDir(baseDir), MAPPER, withTests);
    }

    /**
     * Méthode générique pour générer un composant spécifique
     */
    private void generateComponent(String fqcn, Path baseDir, String componentType, boolean withTests) {
        String[] parts = fqcn.split("\\.");
        String className = parts[parts.length - 1];
        String basePackage = getBasePackage(fqcn);
        String baseAppPackage = getBaseAppPackage(fqcn);
        String subPackage = SUBPACKAGES.get(componentType);

        if (subPackage == null) {
            throw new IllegalArgumentException("Type de composant non supporté : " + componentType);
        }

        // Préparation des données pour le template
        Map<String, Object> data = new HashMap<>();
        data.put("className", className);
        data.put("classNameLower", className.toLowerCase());
        data.put("basePackage", baseAppPackage);
        data.put("packageName", basePackage);

        // Génération du fichier de composant
        String fullPackage = basePackage + "." + subPackage;
        String outputDir = baseDir + "/src/main/java/" + fullPackage.replace('.', '/');
        String outputFile = className + componentType + ".java";
        String templateName = componentType + "Template.java.ftl";

        // Si c'est ServiceImpl, on ajuste le nom du template
        if (SERVICE_IMPL.equals(componentType)) {
            templateName = "ServiceImplTemplate.java.ftl";
        }

        generateFile(cfgMain, templateName, outputDir, outputFile, data);
        System.out.println(GREEN + "Généré: " + outputFile + RESET);

        // Génération des tests si demandé
        if (withTests && shouldGenerateTest(componentType)) {
            String testOutputDir = baseDir + "/src/test/java/" + fullPackage.replace('.', '/');
            String testOutputFile = className + componentType + "Test.java";
            String testTemplate = componentType + "TemplateTest.java.ftl";

            // ServiceImpl n'a pas de test, on utilise le test du Service
            if (SERVICE_IMPL.equals(componentType)) {
                return;
            }

            generateFile(cfgTest, testTemplate, testOutputDir, testOutputFile, data);
            System.out.println(GREEN + "Généré: " + testOutputFile + RESET);
        }
    }

    /**
     * Détermine si on doit générer un test pour le type de composant donné
     */
    private boolean shouldGenerateTest(String componentType) {
        return CONTROLLER.equals(componentType) ||
               SERVICE.equals(componentType) ||
               MAPPER.equals(componentType);
    }

    /**
     * Génère la classe PagedResponse.java si elle n'existe pas déjà
     */
    private void generatePagedResponse(String packageName, Path baseDir) {
        String outputDir = baseDir + "/src/main/java/" + packageName.replace('.', '/') + "/dto/";
        Path dir = Paths.get(outputDir);
        File file = dir.resolve("PagedResponse.java").toFile();

        if (!file.exists()) {
            Map<String, Object> data = Map.of("packageName", packageName);
            generateFile(cfgMain, "PagedResponseTemplate.java.ftl", outputDir, "PagedResponse.java", data);
            System.out.println(GREEN + "Généré: PagedResponse.java" + RESET);
        } else {
            System.out.println(YELLOW + "PagedResponse.java existe déjà, génération ignorée." + RESET);
        }
    }

    /**
     * Génère un fichier à partir d'un template FreeMarker
     */
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
            throw new RuntimeException("Échec de la génération du fichier: " + outputFile, e);
        }
    }

    /**
     * Extrait le package de base à partir du FQCN
     */
    private String getBasePackage(String fqcn) {
        String[] parts = fqcn.split("\\.");
        return String.join(".", Arrays.copyOf(parts, parts.length - 1));
    }

    /**
     * Extrait le package de l'application à partir du FQCN
     */
    private String getBaseAppPackage(String fqcn) {
        String[] parts = fqcn.split("\\.");
        return String.join(".", Arrays.copyOf(parts, parts.length - 2));
    }
}
