plugins {
    `kotlin-dsl`
}

group = "com.archsoftware.afoil.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.modulegraph.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "afoil.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "afoil.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "afoil.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "afoil.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "afoil.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "afoil.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "afoil.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("kotlinxSerialization") {
            id = "afoil.kotlinx.serialization"
            implementationClass = "KotlinSerializationConventionPlugin"
        }
        register("moduleGraph") {
            id = "afoil.modulegraph"
            implementationClass = "ModuleGraphConventionPlugin"
        }
    }
}

