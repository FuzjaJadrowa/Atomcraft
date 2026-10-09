plugins {
    java
    id("net.neoforged.moddev") version "2.0.141"
}

val mod_id: String by project
val mod_version: String by project
val mod_group_id: String by project
val neo_version: String by project
val mod_name: String by project
val minecraft_version: String by project

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
        mappingsVersion = project.property("parchment_mappings_version").toString()
        minecraftVersion = project.property("parchment_minecraft_version").toString()
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
        "mod_name" to project.property("mod_name"),
        "mod_version" to mod_version,
        "mod_description" to project.property("mod_description"),
        "mod_authors" to project.property("mod_authors"),
        "mod_license" to project.property("mod_license"),
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "neo_version_range" to project.property("neo_version_range"),
        "loader_version_range" to project.property("loader_version_range")
    )
    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}
