plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.hilt)
    alias(libs.plugins.afoil.modulegraph)
}

android {
    namespace = "com.archsoftware.afoil.core.data"
}

dependencies {
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.model)
}