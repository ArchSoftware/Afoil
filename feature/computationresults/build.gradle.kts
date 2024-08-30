plugins {
    alias(libs.plugins.afoil.android.feature)
    alias(libs.plugins.afoil.android.library.compose)
    alias(libs.plugins.afoil.modulegraph)
}

android {
    namespace = "com.archsoftware.afoil.feature.computationresults"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.projectstore)
    implementation(projects.core.ui)

    implementation(libs.coil)
}