package sn.consultit.scaffolder;

import java.nio.file.Path;
import java.util.Arrays;

public class Main {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RED = "\u001B[31m";

    public static void main(String[] args) {
        printHeader();

        if (args.length == 0) {
            System.out.println(RED + " Usage: java -jar scaffolder.jar <fqcn> [withTests]" + RESET);
            return;
        }

        String fqcnList = args[0];
        boolean withTests = args.length > 1 && "true".equalsIgnoreCase(args[1]);

        GeneratorService generatorService = new GeneratorService();
        Path baseDir = Path.of("").toAbsolutePath();

        Arrays.stream(fqcnList.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(fqcn -> {
                    System.out.println(PURPLE + "\n----------------------------------------------------");
                    System.out.println("-> Scaffolding : " + CYAN + fqcn + RESET);
                    System.out.println("-> With tests  : " + (withTests ? GREEN + "Yes" : RED + "No") + RESET);
                    System.out.println("-> Base Dir    : " + baseDir);
                    System.out.println("----------------------------------------------------\n" + RESET);

                    generatorService.generateAll(fqcn, baseDir, withTests);
                });

        System.out.println(GREEN + "\n Scaffolding terminé avec succès !" + RESET);
    }

    private static void printHeader() {
        System.out.println(PURPLE +
                "\n    --------------------------------------------------" +
                "\n    |        SPRING BOOT SCAFFOLDER PLUGIN           |" +
                "\n    |        Clean Architecture / MapStruct / DTO    |" +
                "\n    --------------------------------------------------\n" +
                RESET);
    }
}
