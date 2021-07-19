pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.stellardrift.ca/repository/snapshots/")
    }
}

plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0-SNAPSHOT"
}

rootProject.name = "bonk"

include("bukkit")
project(":bukkit").name = "bonk-bukkit"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")