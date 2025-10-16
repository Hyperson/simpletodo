pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

//type-safe accessors e.g : projects.feature.news instead of :feature:news
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "androidstarterkit"

include(":app")
include(":core:testing")

include(":core:common")
include(":core:data")
include(":core:data")
include(":core:model")
include(":core:database")
include(":core:database")
include(":core:network")
include(":feature")
include(":core:domain")
include(":core:designsystem")
include(":feature:mytodos")
include(":feature:settings")
include(":feature:addedittodo")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:datastore-test")
include(":sync:work")
include(":core:speech")


check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    TODO requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
