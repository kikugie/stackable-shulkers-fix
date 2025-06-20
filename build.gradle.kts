plugins {
    id("fabric-loom")
    id("me.fallenbreath.yamlang")
    id("me.modmuss50.mod-publish-plugin")
}

version = "${property("mod.version")}+${property("meta.suffix")}"
base.archivesName = property("mod.name") as String

repositories {
    fun strictMaven(url: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    strictMaven("https://www.cursemaven.com", "curse.maven")
}

dependencies {
    minecraft("com.mojang:minecraft:${stonecutter.current.version}")
    mappings("net.fabricmc:yarn:${property("deps.yarn")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${property("deps.loader")}")
    modImplementation("curse.maven:carpet-349239:${property("deps.carpet")}")
    modLocalRuntime(modCompileOnly("maven.modrinth:lithium:${property("deps.lithium")}")!!)
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft", project.property("meta.compat"))

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft" to project.property("meta.compat")
        )
    }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.map { it.archiveFile })
    into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
    dependsOn("build")
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

publishMods {
    val mr = findProperty("publish.modrinth.key") as? String
    dryRun = mr == null

    type = STABLE
    file = tasks.remapJar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })

    displayName = "ShulkerFix ${property("mod.version")} for ${property("meta.suffix")}"
    version = property("mod.version") as String
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("fabric")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = mr
        minecraftVersions.add(stonecutter.current.version)
        requires("carpet")
        optional("lithium")
    }
}