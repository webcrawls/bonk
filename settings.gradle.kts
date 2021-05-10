rootProject.name = "BingBonk"

include("core", "bukkit")
project(":core").name = "bonk-core"
project(":bukkit").name = "bonk-bukkit"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")