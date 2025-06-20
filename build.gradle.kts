plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("me.fallenbreath.yamlang") version "1.4.1"
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String
base.archivesName = project.property("archives_base_name") as String

repositories {
    fun strictMaven(url: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    strictMaven("https://www.cursemaven.com", "curse.maven")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("curse.maven:carpet-349239:${property("carpet")}")
    modLocalRuntime(modCompileOnly("maven.modrinth:lithium:${property("lithium")}")!!)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

yamlang {
    targetSourceSets = listOf(sourceSets["main"])
    inputDir = "assets/shulkerfix/lang"
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
