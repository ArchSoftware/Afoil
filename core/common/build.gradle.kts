plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.hilt)
    alias(libs.plugins.afoil.modulegraph)
}

android {
    namespace = "com.archsoftware.afoil.core.common"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.runtime.android)

    testImplementation(kotlin("test"))
    testImplementation(projects.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}