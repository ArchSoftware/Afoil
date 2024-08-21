plugins {
    alias(libs.plugins.afoil.android.feature)
    alias(libs.plugins.afoil.android.library.compose)
}

android {
    namespace = "com.archsoftware.afoil.feature.computationmonitor"
}

dependencies {
    implementation(projects.computation.manager)
    implementation(projects.computation.service)
    implementation(projects.core.model)

    testImplementation(projects.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.ui.test.manifest)
}