plugins {
    id("fabric-loom") version "1.16.3"
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
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    val polymerVersion = property("polymer_version")
    modImplementation("eu.pb4:polymer-core:[$polymerVersion]")
    modImplementation("eu.pb4:polymer-blocks:[$polymerVersion]")
    modImplementation("eu.pb4:polymer-resource-pack:[$polymerVersion]")
    modImplementation("eu.pb4:polymer-resource-pack-extras:[$polymerVersion]")
    modImplementation("eu.pb4:polymer-virtual-entity:[$polymerVersion]")
    modImplementation("eu.pb4:polymer-autohost:$polymerVersion")

    modImplementation(include("eu.pb4:factorytools:[${property("factorytools_version")}]")!!)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to inputs.properties["version"]))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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
