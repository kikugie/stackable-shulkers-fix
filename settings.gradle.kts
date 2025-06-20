pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
	}
}

plugins {
	// For some reason, this plugin is crucial - do not remove
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
	id("dev.kikugie.stonecutter") version "0.7-beta.3"
}

stonecutter {
	create(rootProject) {
		versions("1.21.6", "1.21.4")
	}
}