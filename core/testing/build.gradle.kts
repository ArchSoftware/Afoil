plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.hilt)
}

android {
    namespace = "com.archsoftware.afoil.core.testing"
}

dependencies {
    implementation(projects.computation.manager)
    implementation(projects.computation.service)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.model)

    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.kotlinx.coroutines.test)
}