plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.library.compose)
}

android {
    namespace = "com.archsoftware.afoil.core.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
}