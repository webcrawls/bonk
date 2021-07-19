dependencies {
    compileOnly(libs.paper.api)

    api(libs.minimessage)
    api(libs.cloud.paper)
    api(libs.bundles.guice)
    api(libs.bundles.corn)

    implementation(libs.interfaces.paper)
}

repositories {
    maven("https://repo.incendo.org/content/repositories/snapshots/")
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
                "broccolai.corn"
        )

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}