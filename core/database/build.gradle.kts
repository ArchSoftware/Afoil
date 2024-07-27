plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.room)
    alias(libs.plugins.afoil.android.hilt)
}

android {
    namespace = "com.archsoftware.afoil.core.database"
}

dependencies {
    implementation(projects.core.model)
}