pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("com.possible-triangle.helper") version ("1.4")
    id("com.possible-triangle.packwiz") version ("1.4.+")
}

helper {
    versionStrategy.set(com.possible_triangle.gradle.settings.ResolutionStrategy.WILDCARD)
}

include("common")
loader("fabric")

fun loader(vararg names: String) =
    names.forEach {
        include(it)
        packwiz {
            packs.create(it) {
                from = file("$it/pack")
            }
        }
    }
