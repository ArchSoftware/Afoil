package com.archsoftware.afoil

import dev.iurysouza.modulegraph.gradle.ModuleGraphExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Configure ModuleGraph plugin
 */
fun Project.configureModuleGraph() {
    extensions.configure<ModuleGraphExtension> {
        readmePath.set("${projectDir}/README.md")
        heading.set("### Module Graph")
        setStyleByModuleType.set(true)
    }
}