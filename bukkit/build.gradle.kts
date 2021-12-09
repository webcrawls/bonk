dependencies {
    compileOnly(libs.paper.api)
    api(libs.minimessage)
    api(libs.cloud.paper)
    api(libs.bundles.guice)
    api(libs.corn.minecraft.paper)
    api(libs.adventure.bungee)
    implementation(libs.interfaces.paper)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.tab)
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
                "com.google.inject",
                "broccolai.corn",
                "org.incendo.interfaces"
        )

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}