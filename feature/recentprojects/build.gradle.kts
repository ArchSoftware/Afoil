plugins {
    alias(libs.plugins.afoil.android.feature)
    alias(libs.plugins.afoil.android.library.compose)
}

android {
    namespace = "com.archsoftware.afoil.feature.recentprojects"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.projectstore)
    implementation(projects.core.ui)

    testImplementation(kotlin("test"))
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.ui.test.manifest)
}