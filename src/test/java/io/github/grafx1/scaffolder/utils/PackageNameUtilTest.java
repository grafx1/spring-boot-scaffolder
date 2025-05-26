package io.github.grafx1.scaffolder.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PackageNameUtilTest {

    @Test
    void shouldSanitizeArtifactIdWithDashes() {
        // GIVEN
        String groupId = "com.example";
        String artifactId = "my-project";
        String entity = "CustomerMerchant";

        // WHEN
        String packageName = PackageNameUtil.computeBasePackage(groupId, artifactId, entity);

        // THEN
        System.out.println(packageName);
        assertThat(packageName)
                .isEqualTo("com.example.my_project.customerMerchant.CustomerMerchant")
                .doesNotContain("-")
                .startsWith("com.example.my_project");
    }

    @Test
    void shouldFailOnInvalidGroupId() {
        assertThatThrownBy(() -> PackageNameUtil.validateGroupId("com.example_123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid groupId part");
    }

}