package io.github.grafx1.scaffolder.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.grafx1.scaffolder.utils.PackageNameUtil.computeBasePackage;

public class Tools {

    /**
     * Color resetting hexadecimal code
     */
    public static final String RESET = "\u001B[0m";
    /**
     * Green hexadecimal code color
     */
    public static final String GREEN = "\u001B[32m";
    /**
     * Cyan hexadecimal code color
     */
    public static final String CYAN = "\u001B[36m";
    /**
     * Purple hexadecimal code color
     */
    public static final String PURPLE = "\u001B[35m";
    /**
     * Red hexadecimal code color
     */
    public static final String RED = "\u001B[31m";

    /**
     * Yellow hexadecimal code color
     */
    public static final String YELLOW = "\u001B[33m";

    /**
     * Extract groupId and artefactId from pom file
     * @param xml
     * @return
     */
    public static Map<String, String> extractPomInfo(String xml) {
        Map<String, String> result = new HashMap<>();

        String mainSection = xml.replaceAll("(?s)<parent>.*?</parent>", ""); // enleve parent

        Pattern groupPattern = Pattern.compile("<groupId>(.*?)</groupId>");
        Pattern artifactPattern = Pattern.compile("<artifactId>(.*?)</artifactId>");

        Matcher gm = groupPattern.matcher(mainSection);
        Matcher am = artifactPattern.matcher(mainSection);

        result.put("groupId", gm.find() ? gm.group(1).trim() : "com.unknown");
        result.put("artifactId", am.find() ? am.group(1).trim() : "app");

        return result;
    }

    /**
     * Compute FQCN From projet (build.gradle or pom.xml)
     * @param projectDir
     * @param entityName
     * @return
     */
    public static String computeFQCNFromProject(Path projectDir, String entityName) {
        Properties props = new Properties();
        String group = "com.default";
        String appName = "app";

        try {
            Path pom = projectDir.resolve("pom.xml");
            Path gradle = projectDir.resolve("build.gradle");
            Path gradleKts = projectDir.resolve("build.gradle.kts");
            Path settings = projectDir.resolve("settings.gradle");

            if (Files.exists(pom)) {
                String content = Files.readString(pom);
                Map<String, String> meta = extractPomInfo(content);

                group = meta.get("groupId");
                appName = meta.get("artifactId");
            } else if (Files.exists(gradle) || Files.exists(gradleKts)) {
                List<String> lines = Files.readAllLines(Files.exists(gradle) ? gradle : gradleKts);
                for (String line : lines) {
                    if (line.trim().startsWith("group")) {
                        group = line.split("=")[1].trim().replaceAll("['\"]", "");
                        break;
                    }
                }

                List<String> settingLines = Files.readAllLines(settings);
                for (String line : settingLines) {
                    if (line.trim().startsWith("rootProject.name")) {
                        appName = line.split("=")[1].trim().replaceAll("['\"]", "");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de lire les métadonnées du projet");
        }
        return computeBasePackage(group, appName, entityName);
    }

    /**
     * Print help
     */
    public static void printHelp() {
        System.out.println(GREEN + "\n *** Spring Boot Scaffolder Plugin - Help & Hype ***" + RESET);
        System.out.println(GREEN + "--------------------------------------------------" + RESET);
        System.out.println(GREEN + " What does it do?" + RESET);
        System.out.println(GREEN + "  Automatically generates boilerplate Spring Boot code" + RESET);
        System.out.println(GREEN + "  for Entities, DTOs, Services, Controllers, Repositories, etc." + RESET);
        System.out.println(GREEN + "\n-> Usage:" + RESET);
        System.out.println(GREEN + "  ./gradlew scaffold -PentityName=User[,Product,...] [-PwithTests=true]" + RESET);
        System.out.println(GREEN + "\n-> Options:" + RESET);
        System.out.println(GREEN + "    -PentityName     : Name(s) of the entities to scaffold (comma-separated)" + RESET);
        System.out.println(GREEN + "    -PwithTests      : true to generate JUnit tests for each component (optional)" + RESET);
        System.out.println(GREEN + "    -PscaffoldHelp   : true to display this legendary help screen");
        System.out.println(GREEN + "\n-> Examples:" + RESET);
        System.out.println(GREEN + "    ./gradlew scaffold -PentityName=User" + RESET);
        System.out.println(GREEN + "    ./gradlew scaffold -PentityName=\"User,Product\"" + RESET);
        System.out.println(GREEN + "    ./gradlew scaffold -PentityName=Customer -PwithTests=true" + RESET);
        System.out.println(GREEN + "    ./gradlew scaffold -PscaffoldHelp=true" + RESET);
        System.out.println(GREEN + "\n-> Pro tip:" + RESET);
        System.out.println(GREEN + "  You don’t need to specify the package name — we’ll figure it out for you " + RESET);
        System.out.println(GREEN + "--------------------------------------------------" + RESET);
        System.out.println(" Docs: https://github.com/grafx1/spring-boot-boilplate" + RESET);
        System.out.println(" Feedback? Ping @grafx1 with \n" + RESET);
    }

    /**
     * Print header when scaffolding
     */
    public static void printHeader() {
        System.out.println(PURPLE +
                "\n    --------------------------------------------------" +
                "\n    |        SPRING BOOT SCAFFOLDER PLUGIN           |" +
                "\n    |        Clean Architecture / MapStruct / DTO    |" +
                "\n    --------------------------------------------------\n" +
                RESET);
    }

    /**
     * Print Result when scaffold finished
     * @param fqcn
     * @param withTests
     * @param baseDir
     */
    public static void printFooter(String fqcn, boolean withTests, String baseDir) {
        System.out.println(PURPLE + "\n----------------------------------------------------");
        System.out.println("-> Scaffolding : " + CYAN + fqcn + RESET);
        System.out.println("-> With tests  : " + (withTests ? GREEN + "Yes" : RED + "No") + RESET);
        System.out.println("-> Base Dir    : " + baseDir);
        System.out.println("----------------------------------------------------\n" + RESET);
    }
}
