package io.github.grafx1.scaffolder.cli;

import io.github.grafx1.scaffolder.EnhancedGeneratorService;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

import static io.github.grafx1.scaffolder.utils.Tools.*;

/**
 * CLI amélioré pour Spring Boot Scaffolder permettant une utilisation plus interactive
 * et des commandes à la façon d'Angular CLI ou Spring CLI.
 */
public class SpringBootScaffolderCLI {

    private static final String GENERATE_COMMAND = "generate";
    private static final String GENERATE_ALIAS = "g";

    private static final String ENTITY_TYPE = "entity";
    private static final String ENTITY_ALIAS = "e";
    private static final String DTO_TYPE = "dto";
    private static final String DTO_ALIAS = "d";
    private static final String REPOSITORY_TYPE = "repository";
    private static final String REPOSITORY_ALIAS = "r";
    private static final String SERVICE_TYPE = "service";
    private static final String SERVICE_ALIAS = "s";
    private static final String CONTROLLER_TYPE = "controller";
    private static final String CONTROLLER_ALIAS = "c";
    private static final String MAPPER_TYPE = "mapper";
    private static final String MAPPER_ALIAS = "m";
    private static final String CRUD_TYPE = "crud";
    private static final String ALL_TYPE = "all";

    private final EnhancedGeneratorService generatorService;
    private final Path baseDir;

    public SpringBootScaffolderCLI() {
        this.generatorService = new EnhancedGeneratorService();
        this.baseDir = Path.of("").toAbsolutePath();
    }

    /**
     * Point d'entrée principal du CLI
     */
    public static void main(String[] args) {
        printHeader();

        SpringBootScaffolderCLI cli = new SpringBootScaffolderCLI();

        if (args.length == 0) {
            cli.runInteractiveMode();
        } else {
            cli.parseCommands(args);
        }
    }

    /**
     * Démarre le mode interactif du CLI
     */
    private void runInteractiveMode() {
        System.out.println(CYAN + "Mode interactif de Spring Boot Scaffolder" + RESET);
        System.out.println(YELLOW + "Tapez 'exit' ou 'quit' pour quitter, 'help' pour l'aide" + RESET);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print(GREEN + "spring> " + RESET);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                running = false;
                System.out.println(YELLOW + "Au revoir!" + RESET);
            } else if (input.equalsIgnoreCase("help")) {
                printCliHelp();
            } else if (!input.isEmpty()) {
                parseCommands(input.split("\\s+"));
            }
        }

        scanner.close();
    }

    /**
     * Analyse et exécute les commandes passées en arguments
     */
    private void parseCommands(String[] args) {
        if (args.length == 0) {
            printCliHelp();
            return;
        }

        String command = args[0].toLowerCase();

        // Gestion de la commande d'aide
        if (command.equals("help") || command.equals("--help") || command.equals("-h")) {
            printCliHelp();
            return;
        }

        // Gestion de la commande de génération
        if (command.equals(GENERATE_COMMAND) || command.equals(GENERATE_ALIAS)) {
            if (args.length < 3) {
                System.out.println(RED + "Erreur: Les commandes generate nécessitent un type et un nom" + RESET);
                printCliHelp();
                return;
            }

            String type = args[1].toLowerCase();
            String names = args[2];
            boolean withTests = args.length > 3 && args[3].equalsIgnoreCase("--withTests");

            generateByType(type, names, withTests);
        } else {
            System.out.println(RED + "Commande non reconnue: " + command + RESET);
            printCliHelp();
        }
    }

    /**
     * Génère les fichiers en fonction du type demandé
     */
    private void generateByType(String type, String names, boolean withTests) {
        switch (type) {
            case ENTITY_TYPE:
            case ENTITY_ALIAS:
                generateEntityOnly(names);
                break;
            case DTO_TYPE:
            case DTO_ALIAS:
                generateDtoOnly(names);
                break;
            case REPOSITORY_TYPE:
            case REPOSITORY_ALIAS:
                generateRepositoryOnly(names);
                break;
            case SERVICE_TYPE:
            case SERVICE_ALIAS:
                generateServiceOnly(names);
                break;
            case CONTROLLER_TYPE:
            case CONTROLLER_ALIAS:
                generateControllerOnly(names);
                break;
            case MAPPER_TYPE:
            case MAPPER_ALIAS:
                generateMapperOnly(names);
                break;
            case CRUD_TYPE:
            case ALL_TYPE:
                generateAllLayers(names, withTests);
                break;
            default:
                System.out.println(RED + "Type non reconnu: " + type + RESET);
                printCliHelp();
        }
    }

    /**
     * Génère uniquement les entités
     */
    private void generateEntityOnly(String names) {
        System.out.println(CYAN + "Génération des entités: " + names + RESET);
        processNamesForComponent(names, false, (fqcn, withTests) ->
            generatorService.generateEntityComponent(fqcn, baseDir, withTests));
    }

    /**
     * Génère uniquement les DTOs
     */
    private void generateDtoOnly(String names) {
        System.out.println(CYAN + "Génération des DTOs: " + names + RESET);
        processNamesForComponent(names, false, (fqcn, withTests) ->
            generatorService.generateDtoComponent(fqcn, baseDir, withTests));
    }

    /**
     * Génère uniquement les repositories
     */
    private void generateRepositoryOnly(String names) {
        System.out.println(CYAN + "Génération des repositories: " + names + RESET);
        processNamesForComponent(names, false, (fqcn, withTests) ->
            generatorService.generateRepositoryComponent(fqcn, baseDir, withTests));
    }

    /**
     * Génère uniquement les services
     */
    private void generateServiceOnly(String names) {
        System.out.println(CYAN + "Génération des services: " + names + RESET);
        processNamesForComponent(names, false, (fqcn, withTests) -> {
            generatorService.generateServiceComponent(fqcn, baseDir, withTests);
            generatorService.generateServiceImplComponent(fqcn, baseDir, withTests);
        });
    }

    /**
     * Génère uniquement les controllers
     */
    private void generateControllerOnly(String names) {
        System.out.println(CYAN + "Génération des controllers: " + names + RESET);
        processNamesForComponent(names, false, (fqcn, withTests) ->
            generatorService.generateControllerComponent(fqcn, baseDir, withTests));
    }

    /**
     * Génère uniquement les mappers
     */
    private void generateMapperOnly(String names) {
        System.out.println(CYAN + "Génération des mappers: " + names + RESET);
        processNamesForComponent(names, false, (fqcn, withTests) ->
            generatorService.generateMapperComponent(fqcn, baseDir, withTests));
    }

    /**
     * Génère tous les composants pour les entités spécifiées (CRUD complet)
     */
    private void generateAllLayers(String names, boolean withTests) {
        System.out.println(CYAN + "Génération de la structure CRUD complète pour: " + names + RESET);
        System.out.println(withTests ?
                GREEN + "Les tests unitaires seront également générés" + RESET :
                YELLOW + "Les tests unitaires ne seront pas générés" + RESET);

        // Utilise le générateur existant pour créer tous les fichiers
        generatorService.generateStructure(names, baseDir, withTests);
    }

    /**
     * Interface fonctionnelle pour traiter chaque entité
     */
    @FunctionalInterface
    private interface EntityProcessor {
        void process(String fqcn, boolean withTests);
    }

    /**
     * Traite chaque nom d'entité et applique le processeur
     */
    private void processNamesForComponent(String names, boolean withTests, EntityProcessor processor) {
        Arrays.stream(names.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .forEach(entity -> {
                if (entity.split("\\.").length > 1) {
                    printHelp();
                    throw new IllegalArgumentException("Invalid entity name : " + entity);
                }
                String fqcn = computeFQCNFromProject(baseDir, entity);
                System.out.println("fqcn : " + fqcn);

                // Appliquer le processeur pour cette entité
                processor.process(fqcn, withTests);
            });

        System.out.println(GREEN + "\nGénération terminée avec succès!" + RESET);
    }

    /**
     * Affiche l'aide du CLI
     */
    private void printCliHelp() {
        System.out.println("\n" + CYAN + "Spring Boot Scaffolder CLI - Guide d'utilisation" + RESET);
        System.out.println("\nCommandes disponibles:");
        System.out.println(GREEN + "  spring generate|g crud <nom1>[,nom2,...] [--withTests]" + RESET + " - Génère une structure CRUD complète");
        System.out.println(GREEN + "  spring generate|g entity|e <nom1>[,nom2,...]" + RESET + " - Génère uniquement des entités");
        System.out.println(GREEN + "  spring generate|g dto|d <nom1>[,nom2,...]" + RESET + " - Génère uniquement des DTOs");
        System.out.println(GREEN + "  spring generate|g repository|r <nom1>[,nom2,...]" + RESET + " - Génère uniquement des repositories");
        System.out.println(GREEN + "  spring generate|g service|s <nom1>[,nom2,...]" + RESET + " - Génère uniquement des services");
        System.out.println(GREEN + "  spring generate|g controller|c <nom1>[,nom2,...]" + RESET + " - Génère uniquement des controllers");
        System.out.println(GREEN + "  spring generate|g mapper|m <nom1>[,nom2,...]" + RESET + " - Génère uniquement des mappers");
        System.out.println(GREEN + "  help" + RESET + " - Affiche ce guide d'utilisation");
        System.out.println(GREEN + "  exit|quit" + RESET + " - Quitte le programme en mode interactif");

        System.out.println("\nExemples:");
        System.out.println(YELLOW + "  spring g crud User,Product --withTests" + RESET);
        System.out.println(YELLOW + "  spring generate entity Customer" + RESET);
        System.out.println(YELLOW + "  spring g c Order,Invoice" + RESET);
        System.out.println();
    }
}
