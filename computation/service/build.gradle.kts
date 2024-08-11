plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.hilt)
}

android {
    namespace = "com.archsoftware.afoil.computation.service"
}

dependencies {
    implementation(projects.computation.manager)
    implementation(projects.core.model)
    implementation(projects.core.notifications)

    implementation(libs.androidx.core.ktx)
}