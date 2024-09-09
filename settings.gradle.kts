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
        val useLocalRepo: String? by settings
        if (useLocalRepo.toBoolean()) {
            mavenLocal()
        } else {
            maven {
                url = uri("https://maven.pkg.github.com/ArchSoftware/afoil-engine")
                credentials {
                    val gprUser: String? by settings
                    val gprKey: String? by settings
                    username = gprUser ?: System.getenv("GITHUB_USERNAME")
                    password = gprKey ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
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
include(":feature:computationresults")
include(":feature:recentprojects")
 