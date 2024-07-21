plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.library.compose)
}

android {
    namespace = "com.archsoftware.afoil.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(libs.androidx.material3)
    api(libs.androidx.material.icons.extended)

    androidTestImplementation(libs.androidx.espresso.core)
}