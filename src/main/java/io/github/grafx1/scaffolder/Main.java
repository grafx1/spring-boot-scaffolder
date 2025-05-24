package io.github.grafx1.scaffolder;

import java.nio.file.Path;
import java.util.Arrays;

import static io.github.grafx1.scaffolder.utils.Tools.*;

public class Main {


    public static void main(String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("sHelp") || args[0].equalsIgnoreCase("--sHelp")) {
            printHelp();
            return;
        }

        printHeader();

        String entityArg = args[0];
        boolean withTests = args.length > 1 && "true".equalsIgnoreCase(args[1]);

        GeneratorService generatorService = new GeneratorService();
        Path baseDir = Path.of("").toAbsolutePath();
        generatorService.generateStructure(entityArg, baseDir, withTests);

    }


}
