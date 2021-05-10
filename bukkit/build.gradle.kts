dependencies {
    api(projects.bonkApi)
    api(projects.bonkCore)

    compileOnly(libs.paper)

    api(libs.adventure.bukkit)
    api(libs.minimessage)
    api(libs.cloud.paper)
}

tasks {

    build {
        dependsOn(named("shadowJar"))
    }

    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split(".")
                val name = split.last()
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
            exclude(dependency("org.checkerframework:"))
        }

        relocates(
                "cloud.commandframework",
                "net.kyori",
                "com.google.inject",
        )

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}