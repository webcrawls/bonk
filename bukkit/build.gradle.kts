dependencies {
    api(projects.bonkCore)
}

tasks {
    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split(".")
                val name = split.last()
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

//        dependencies {
//
//        }

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}