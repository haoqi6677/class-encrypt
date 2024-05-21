package fuck.world.plugin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReCompilePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // 添加一个任务
        project.tasks.register("reCompilePlugin", ReCompileTask.class)
    }
}
