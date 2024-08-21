pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Afoil"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":computation:manager")
include(":computation:service")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:model")
include(":core:notifications")
include(":core:projectstore")
include(":core:ui")
include(":core:testing")
include(":feature:airfoilanalysis")
include(":feature:computationmonitor")
 