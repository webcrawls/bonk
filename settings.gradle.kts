rootProject.name = "BingBonk"

include("core", "bukkit", "api")
project(":core").name = "bonk-core"
project(":bukkit").name = "bonk-bukkit"
project(":api").name = "bonk-api"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")