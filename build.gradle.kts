plugins {
    java
    id("net.neoforged.moddev") version "2.0.141"
}

val mod_id = providers.gradleProperty("mod_id").get()
val mod_version = providers.gradleProperty("mod_version").get()
val mod_group_id = providers.gradleProperty("mod_group_id").get()
val neo_version = providers.gradleProperty("neo_version").get()
val mod_name = providers.gradleProperty("mod_name").get()
val minecraft_version = providers.gradleProperty("minecraft_version").get()

group = mod_group_id
version = mod_version

base {
    archivesName.set(mod_id)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.jar {
    archiveFileName.set("$mod_name-$mod_version+$minecraft_version.jar")
}

neoForge {
    version = neo_version

    parchment {
        mappingsVersion = providers.gradleProperty("parchment_mappings_version").get()
        minecraftVersion = providers.gradleProperty("parchment_minecraft_version").get()
    }

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "debug")
        }

        create("client") {
            client()
        }

        create("server") {
            server()
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_version" to mod_version,
        "mod_description" to providers.gradleProperty("mod_description").get(),
        "mod_authors" to providers.gradleProperty("mod_authors").get(),
        "mod_license" to providers.gradleProperty("mod_license").get(),
        "minecraft_version_range" to providers.gradleProperty("minecraft_version_range").get(),
        "neo_version_range" to providers.gradleProperty("neo_version_range").get(),
        "loader_version_range" to providers.gradleProperty("loader_version_range").get()
    )
    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}