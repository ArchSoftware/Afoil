plugins {
    alias(libs.plugins.afoil.android.library)
    alias(libs.plugins.afoil.android.hilt)
    alias(libs.plugins.afoil.modulegraph)
}

android {
    namespace = "com.archsoftware.afoil.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore)
}