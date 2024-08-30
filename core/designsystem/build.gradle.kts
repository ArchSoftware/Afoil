plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.library.compose)
    alias(libs.plugins.afoil.modulegraph)
}

android {
    namespace = "com.archsoftware.afoil.core.designsystem"
}

dependencies {
    api(libs.androidx.material3)
    api(libs.androidx.material.icons.extended)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.ui.test.manifest)
}