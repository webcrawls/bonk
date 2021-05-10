import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin

plugins {
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "dev.kscott.bing"
version = "1.0.0"

subprojects {
    apply {
        plugin<ShadowPlugin>()
    }

    repositories {
        mavenCentral()
    }
}