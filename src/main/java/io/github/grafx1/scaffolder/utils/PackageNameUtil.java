package io.github.grafx1.scaffolder.utils;

public class PackageNameUtil {

    private static final String PACKAGE_PART_REGEX = "^[a-zA-Z][a-zA-Z0-9]*$";

    public static String computeBasePackage(String groupId, String artifactId, String entityName) {
        //validateGroupId(groupId);

        String sanitizedArtifactId = artifactId.replace("-", "_"); // on continue de normaliser ici
        String sanitizedGroupIdId = groupId.replace("-", "_"); // on continue de normaliser ici
        String sanitizedEntity = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);

        return String.join(".", sanitizedGroupIdId, sanitizedArtifactId, sanitizedEntity, entityName);
    }

    public static void validateGroupId(String groupId) {
        String[] parts = groupId.split("\\.");
        for (String part : parts) {
            if (!part.matches(PACKAGE_PART_REGEX)) {
                throw new IllegalArgumentException("Invalid groupId part: '" + part + "' — each part must match [a-zA-Z][a-zA-Z0-9]*");
            }
        }
    }
}
