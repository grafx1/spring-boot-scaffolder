package sn.consultit.scaffolder;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Path;

public class ScaffoldTask extends DefaultTask {

    @TaskAction
    public void generate() {
        String help = (String) getProject().findProperty("showHelp");
        if ("true".equalsIgnoreCase(help)) {
            printHelp();
            return;
        }

        String fqcnList = (String) getProject().findProperty("fqcn");
        if (fqcnList == null || fqcnList.isBlank()) {
            System.err.println("Erreur : aucune classe spécifiée !");
            printHelp();
            throw new IllegalArgumentException("Aucune FQCN n'a été fournie. Utilisez -Pfqcn=... ou -Phelp=true");
        }

        String[] fqcnArray = fqcnList.split(",");

        GeneratorService generatorService = new GeneratorService();
        Path projectRoot = getProject().getProjectDir().toPath();

        for (String fqcn : fqcnArray) {
            fqcn = fqcn.trim();
            if (!fqcn.isEmpty()) {
                System.out.println(" Génération : " + fqcn);
                generatorService.generateAll(fqcn, projectRoot);
            }
        }
    }

    private void printHelp() {
        System.out.println("\n Spring Boot Scaffolder Plugin - Aide");
        System.out.println("Usage :");
        System.out.println("  ./gradlew scaffold -Pfqcn=sn.example.User[,sn.example.Product,...]");
        System.out.println("Options :");
        System.out.println("  -Pfqcn     : Nom(s) complet(s) de classe à générer (séparés par virgule)");
        System.out.println("  -Phelp=true : Affiche cette aide");
        System.out.println("\nExemples :");
        System.out.println("  ./gradlew scaffold -Pfqcn=sn.example.User");
        System.out.println("  ./gradlew scaffold -Pfqcn=\"sn.example.User,sn.example.Product\"");
        System.out.println("  ./gradlew scaffold -Phelp=true\n");
    }
}
