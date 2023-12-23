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
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS") // enables projects accessor
rootProject.name = "Pimodoro"
include(":app")
include(":feature:settings")
include(":feature:timer")
include(":core:model")
include(":core:datastore")
include(":core:common")
include(":core:designsystem")
include(":core:data")
include(":feature:pomodoro")
include(":core:manager")
include(":core:domain")
include(":core:service")
