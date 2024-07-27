
import com.archsoftware.afoil.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("afoil.android.library")
                apply("afoil.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:ui"))

                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())

                add("androidTestImplementation", kotlin("test"))
                add("androidTestImplementation", libs.findLibrary("androidx.ui.test.junit4").get())
            }
        }
    }
}