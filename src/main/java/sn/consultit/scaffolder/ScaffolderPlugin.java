package sn.consultit.scaffolder;

import org.gradle.api.*;

public class ScaffolderPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().create("scaffold", ScaffoldTask.class, task -> {
            task.setGroup("scaffolding");
            task.setDescription("Scaffold a new feature layer by package.ClassName");
        });
    }
}
