import com.archsoftware.afoil.configureModuleGraph
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

class ModuleGraphConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            val hasProjectDependencies =
                configurations.getByName("implementation").dependencies.any {
                    it is ProjectDependency
                }
            // Configure ModuleGraph plugin for modules with at least one project dependency only
            // otherwise IllegalArgumentException is thrown
            if (hasProjectDependencies) {
                with(pluginManager) {
                    apply("dev.iurysouza.modulegraph")
                }

                configureModuleGraph()
            }
        }
    }
}