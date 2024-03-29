pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        maven (url = "https://jitpack.io" )
//        maven (url = "https://oss.sonatype.org/content/repositories/snapshots/" )
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven (url = "https://jitpack.io" )
//        maven (url = "https://oss.sonatype.org/content/repositories/snapshots/" )
    }
}

rootProject.name = "SignalCopy"
include(":app")
