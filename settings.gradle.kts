pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.9.22"
    }
}
rootProject.name = "duyuruBot"

include("chattools")
include("duyuru-infra")
include("duyuru")