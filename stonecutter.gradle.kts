plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.10-SNAPSHOT" apply false
    id("me.fallenbreath.yamlang") version "1.4.1" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.8.+" apply false
}
stonecutter active "1.21.6"

stonecutter tasks {
    order("publishModrinth")
}