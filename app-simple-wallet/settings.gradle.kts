pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencyResolutionManagement {
        repositories {
            google()
            mavenCentral()
            // mavenLocal()
        }
    }
}

rootProject.name = "Devkit Wallet — Simple"
include("app")
