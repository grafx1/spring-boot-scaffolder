package io.github.grafx1.scaffolder.cli;

import io.github.grafx1.scaffolder.utils.Tools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests pour le CLI amélioré de Spring Boot Scaffolder
 * Les tests sont exécutés dans un dossier temporaire complètement isolé
 * pour éviter d'interférer avec la structure du projet principal
 */
public class SpringBootScaffolderCLITest {

    @TempDir
    Path tempDir;

    private ByteArrayOutputStream outputCaptor;
    private PrintStream originalOut;
    private String originalUserDir;
    private String originalOutputDir;

    /**
     * Configuration à faire avant chaque test
     */
    @BeforeEach
    public void setup() throws IOException {
        System.err.println("======== TEST SETUP STARTING ========");

        // Sauvegarde du dossier utilisateur actuel
        originalUserDir = System.getProperty("user.dir");
        System.err.println("Original user.dir: " + originalUserDir);

        // Sauvegarde du dossier de sortie s'il est défini
        originalOutputDir = System.getProperty("scaffolder.output.dir");
        System.err.println("Original scaffolder.output.dir: " + originalOutputDir);

        // Capture la sortie standard pour les assertions
        outputCaptor = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputCaptor));

        // Crée une structure minimale d'application Spring Boot pour les tests
        createMinimalSpringBootAppStructure(tempDir);
        System.err.println("Created minimal Spring Boot structure in: " + tempDir);

        // Configure le test pour utiliser le dossier temporaire comme dossier courant
        System.setProperty("user.dir", tempDir.toString());
        System.err.println("Set user.dir to: " + tempDir);

        // Configure le dossier de sortie pour que les fichiers générés aillent dans un sous-dossier du dossier temporaire
        Path outputDir = tempDir.resolve("output");
        Files.createDirectories(outputDir);
        System.setProperty("scaffolder.output.dir", outputDir.toString());
        System.err.println("Set scaffolder.output.dir to: " + outputDir);

        System.err.println("======== TEST SETUP COMPLETE ========");
    }

    /**
     * Nettoyage à faire après chaque test
     */
    @AfterEach
    public void tearDown() {
        System.err.println("======== TEST TEARDOWN STARTING ========");

        // Restaure la sortie standard
        System.setOut(originalOut);
        System.err.println("Restored original System.out");

        // Restaure le dossier utilisateur d'origine
        System.setProperty("user.dir", originalUserDir);
        System.err.println("Restored user.dir to: " + originalUserDir);

        // Restaure le dossier de sortie d'origine
        if (originalOutputDir != null) {
            System.setProperty("scaffolder.output.dir", originalOutputDir);
            System.err.println("Restored scaffolder.output.dir to: " + originalOutputDir);
        } else {
            System.clearProperty("scaffolder.output.dir");
            System.err.println("Cleared scaffolder.output.dir property");
        }

        System.err.println("Test directory: " + tempDir);

        try {
            // Liste les fichiers générés pour faciliter le débogage (limité aux fichiers Java)
            System.err.println("Generated files:");
            Files.walk(tempDir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> System.err.println("  - " + p));
        } catch (IOException e) {
            System.err.println("Error listing files: " + e.getMessage());
        }

        System.err.println("======== TEST TEARDOWN COMPLETE ========");
    }

    @Test
    public void testEntityGeneration() throws Exception {
        // Configuration du test
        String[] args = {"generate", "entity", "User"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Attendre un peu pour s'assurer que les fichiers sont créés
        Thread.sleep(500);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération des entités: User");

        // Le chemin exact des fichiers dépend de la façon dont le package est déterminé dans votre application
        // Utilisons une approche plus générique pour trouver le fichier généré
        List<Path> entityFiles = findGeneratedFiles(tempDir, "UserEntity.java");
        assertThat(entityFiles).isNotEmpty();
        assertThat(entityFiles.get(0).toString()).contains("entity");
    }

    @Test
    public void testDtoGeneration() throws Exception {
        // Configuration du test
        String[] args = {"g", "dto", "Product"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Attendre un peu pour s'assurer que les fichiers sont créés
        Thread.sleep(500);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération des DTOs: Product");

        // Approche générique pour trouver le fichier
        List<Path> dtoFiles = findGeneratedFiles(tempDir, "ProductDto.java");
        assertThat(dtoFiles).isNotEmpty();
        assertThat(dtoFiles.get(0).toString()).contains("dto");
    }

    @Test
    public void testRepositoryGeneration() throws Exception {
        // Configuration du test
        String[] args = {"g", "r", "Order"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Attendre un peu pour s'assurer que les fichiers sont créés
        Thread.sleep(500);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération des repositories: Order");

        // Approche générique pour trouver le fichier
        List<Path> repoFiles = findGeneratedFiles(tempDir, "OrderRepository.java");
        assertThat(repoFiles).isNotEmpty();
        assertThat(repoFiles.get(0).toString()).contains("repository");
    }

    @Test
    public void testControllerGeneration() throws Exception {
        // Configuration du test
        String[] args = {"g", "c", "Customer"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Attendre un peu pour s'assurer que les fichiers sont créés
        Thread.sleep(500);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération des controllers: Customer");

        // Approche générique pour trouver le fichier
        List<Path> controllerFiles = findGeneratedFiles(tempDir, "CustomerController.java");
        assertThat(controllerFiles).isNotEmpty();
        assertThat(controllerFiles.get(0).toString()).contains("controller");
    }

    @Test
    public void testMapperGeneration() throws Exception {
        // Configuration du test
        String[] args = {"g", "m", "Invoice"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Attendre un peu pour s'assurer que les fichiers sont créés
        Thread.sleep(500);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération des mappers: Invoice");

        // Approche générique pour trouver le fichier
        List<Path> mapperFiles = findGeneratedFiles(tempDir, "InvoiceMapper.java");
        assertThat(mapperFiles).isNotEmpty();
        assertThat(mapperFiles.get(0).toString()).contains("mapper");
    }

    @Test
    public void testServiceGeneration() throws Exception {
        // Configuration du test
        String[] args = {"g", "s", "Payment"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Attendre un peu pour s'assurer que les fichiers sont créés
        Thread.sleep(500);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération des services: Payment");

        // Approche générique pour trouver les fichiers
        List<Path> serviceFiles = findGeneratedFiles(tempDir, "PaymentService.java");
        List<Path> serviceImplFiles = findGeneratedFiles(tempDir, "PaymentServiceImpl.java");

        assertThat(serviceFiles).isNotEmpty();
        assertThat(serviceImplFiles).isNotEmpty();
        assertThat(serviceFiles.get(0).toString()).contains("service");
        assertThat(serviceImplFiles.get(0).toString()).contains("service" + File.separator + "impl");
    }

    @Test
    public void testCrudGeneration() throws Exception {
        // Configuration du test - générer sans tests pour éviter les problèmes de compilation
        // car les tests générés dépendent de classes qui n'existent pas dans l'environnement de test
        String[] args = {"g", "crud", "Booking"}; // Sans l'option --withTests

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Attendre un peu pour s'assurer que les fichiers sont créés
        Thread.sleep(1000);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération de la structure CRUD complète pour: Booking");

        // Recherche par nom de fichier, plus robuste que des chemins hardcodés
        List<Path> entityFiles = findGeneratedFiles(tempDir, "BookingEntity.java");
        List<Path> dtoFiles = findGeneratedFiles(tempDir, "BookingDto.java");
        List<Path> repoFiles = findGeneratedFiles(tempDir, "BookingRepository.java");
        List<Path> serviceFiles = findGeneratedFiles(tempDir, "BookingService.java");
        List<Path> implFiles = findGeneratedFiles(tempDir, "BookingServiceImpl.java");
        List<Path> controllerFiles = findGeneratedFiles(tempDir, "BookingController.java");
        List<Path> mapperFiles = findGeneratedFiles(tempDir, "BookingMapper.java");

        // Vérifier que les fichiers principaux sont générés
        assertThat(entityFiles).isNotEmpty();
        assertThat(dtoFiles).isNotEmpty();
        assertThat(repoFiles).isNotEmpty();
        assertThat(serviceFiles).isNotEmpty();
        assertThat(implFiles).isNotEmpty();
        assertThat(controllerFiles).isNotEmpty();
        assertThat(mapperFiles).isNotEmpty();
    }

    /**
     * Test spécifique pour la fonctionnalité de génération avec tests unitaires
     * Ce test vérifie seulement que l'option est reconnue et que les messages de sortie sont corrects
     * mais n'évalue pas la génération effective des fichiers de test pour éviter les problèmes de compilation
     */
    @Test
    public void testCrudGenerationWithTestsOption() throws Exception {
        // Configuration du test - générer avec l'option tests
        String[] args = {"g", "crud", "Product", "--withTests"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Vérifications des messages uniquement
        String output = outputCaptor.toString();
        assertThat(output).contains("Génération de la structure CRUD complète pour: Product");
        assertThat(output).contains("Les tests unitaires seront également générés");
    }

    @Test
    public void testHelpCommand() {
        // Configuration du test
        String[] args = {"help"};

        // Exécution du CLI
        SpringBootScaffolderCLI.main(args);

        // Vérifications
        String output = outputCaptor.toString();
        assertThat(output).contains("Spring Boot Scaffolder CLI - Guide d'utilisation");
    }

    /**
     * Utilitaire pour parcourir un dossier récursivement et trouver des fichiers par nom
     */
    private List<Path> findGeneratedFiles(Path rootDir, String fileName) throws IOException {
        return Files.walk(rootDir)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().equals(fileName))
                .collect(Collectors.toList());
    }

    /**
     * Crée une structure minimale d'application Spring Boot pour les tests
     * Cette structure sera utilisée comme base pour la génération de code
     */
    private void createMinimalSpringBootAppStructure(Path rootDir) throws IOException {
        // Crée les répertoires nécessaires
        Path srcMainJava = rootDir.resolve("src/main/java");
        Path srcMainResources = rootDir.resolve("src/main/resources");
        Path srcTestJava = rootDir.resolve("src/test/java");
        Path packagePath = srcMainJava.resolve("com/example/demo");

        Files.createDirectories(srcMainJava);
        Files.createDirectories(srcMainResources);
        Files.createDirectories(srcTestJava);
        Files.createDirectories(packagePath);

        // Crée un fichier Application.java minimal
        Path appFilePath = packagePath.resolve("DemoApplication.java");
        String appContent = "package com.example.demo;\n\n" +
                "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n" +
                "@SpringBootApplication\n" +
                "public class DemoApplication {\n\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(DemoApplication.class, args);\n" +
                "    }\n" +
                "}\n";
        Files.writeString(appFilePath, appContent);

        // Crée un fichier application.properties minimal
        Path propsPath = srcMainResources.resolve("application.properties");
        String propsContent = "spring.application.name=demo\n";
        Files.writeString(propsPath, propsContent);

        // Crée un fichier build.gradle minimal
        Path buildFilePath = rootDir.resolve("build.gradle");
        String buildContent = "plugins {\n" +
                "    id 'org.springframework.boot' version '3.2.0'\n" +
                "    id 'io.spring.dependency-management' version '1.1.4'\n" +
                "    id 'java'\n" +
                "}\n\n" +
                "group = 'com.example'\n" +
                "version = '0.0.1-SNAPSHOT'\n" +
                "sourceCompatibility = '17'\n\n" +
                "repositories {\n" +
                "    mavenCentral()\n" +
                "}\n\n" +
                "dependencies {\n" +
                "    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'\n" +
                "    implementation 'org.springframework.boot:spring-boot-starter-web'\n" +
                "    implementation 'org.mapstruct:mapstruct:1.6.3'\n" +
                "    annotationProcessor 'org.mapstruct:mapstruct-processor:1.6.3'\n" +
                "    compileOnly 'org.projectlombok:lombok'\n" +
                "    annotationProcessor 'org.projectlombok:lombok'\n" +
                "}\n";
        Files.writeString(buildFilePath, buildContent);

        // Ajoute un settings.gradle pour compléter la configuration du projet
        Path settingsPath = rootDir.resolve("settings.gradle");
        String settingsContent = "rootProject.name = 'demo'\n";
        Files.writeString(settingsPath, settingsContent);
    }
}
