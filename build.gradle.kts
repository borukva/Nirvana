plugins {
    // 26.2 has no official mapping file to fetch (its jar already ships with real names -
    // nothing to deobfuscate), so no mappings(...) dependency is declared below - Loom
    // auto-detects the already-official jar and needs nothing further, PROVIDED the plugin is
    // applied by its full id. The short alias "fabric-loom" resolves to a different plugin
    // marker that does NOT auto-detect this and fails with "Configuration 'mappings' has no
    // dependencies" - confirmed by isolated testing against a known-working MC 26.1 project.
    id("net.fabricmc.fabric-loom") version "1.15.5"
    id("maven-publish")
}

version = property("mod_version") as String
group = property("maven_group") as String

base {
    archivesName.set(property("archives_base_name") as String)
}

repositories {
    maven("https://maven.nucleoid.xyz")
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    // Minecraft 26.x ships with official (Mojang) names baked in already - there is nothing left
    // to deobfuscate, and unlike 1.21.x there is no separate mapping file Mojang even publishes
    // for it (confirmed against the version manifest: only `client`/`server` downloads, no
    // `client_mappings`/`server_mappings`). Loom auto-detects this and needs no mappings(...)
    // dependency declared at all.
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    // The umbrella fabric-api artifact's POM lists this as a compile dependency, but Loom
    // doesn't seem to resolve it onto the main compileClasspath transitively for 26.2 - our own
    // datagen/ package (registered via the fabric-datagen entrypoint) needs it directly.
    implementation("net.fabricmc.fabric-api:fabric-data-generation-api-v1:25.5.0+aed122aa9e")

    val polymerVersion = property("polymer_version")
    implementation("eu.pb4:polymer-core:[$polymerVersion]")
    implementation("eu.pb4:polymer-blocks:[$polymerVersion]")
    implementation("eu.pb4:polymer-resource-pack:[$polymerVersion]")
    implementation("eu.pb4:polymer-resource-pack-extras:[$polymerVersion]")
    implementation("eu.pb4:polymer-virtual-entity:[$polymerVersion]")
    implementation("eu.pb4:polymer-autohost:$polymerVersion")

    implementation(include("eu.pb4:factorytools:[${property("factorytools_version")}]")!!)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to inputs.properties["version"]))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
    options.compilerArgs.add("-Xmaxerrs")
    options.compilerArgs.add("2000")
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE.md") {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = property("archives_base_name") as String
            from(components["java"])
        }
    }

    repositories {
        // Add repositories to publish to here.
    }
}
