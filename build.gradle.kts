import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin

plugins {
    id("net.kyori.indra") version "1.3.1"
    id("net.kyori.indra.publishing") version "1.3.1"
    id("net.kyori.indra.checkstyle") version "1.3.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"}

group = "dev.kscott.bing"
version = "1.0.0"

subprojects {
    apply {
        plugin<ShadowPlugin>()
        plugin<IndraPlugin>()
        plugin<IndraCheckstylePlugin>()
        plugin<IndraPublishingPlugin>()
    }

    repositories {
        mavenCentral()

        maven("https://papermc.io/repo/repository/maven-public/")
    }

    tasks {

        indra {
            gpl3OnlyLicense()

            javaVersions {
                target.set(11)
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }
    }
}