import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.sonatypeSnapshots

plugins {
    id("net.kyori.indra") version "1.3.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("com.github.ben-manes.versions") version "0.38.0"
}

group = "dev.kscott"
version = "1.0.0"

subprojects {
    apply {
        plugin<ShadowPlugin>()
        plugin<IndraPlugin>()
    }

    repositories {
        mavenCentral()
        sonatypeSnapshots()

        maven("https://repo.incendo.org/content/repositories/snapshots/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.broccol.ai")
        maven("https://repo.broccol.ai/snapshots")
        mavenLocal()
    }

    tasks {
        indra {
            gpl3OnlyLicense()

            javaVersions {
                target.set(16)
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }
    }
}

allprojects {
    tasks.findByPath(":bonk-bukkit:javadoc")?.enabled = false
}