plugins {
    id("groovy-gradle-plugin")
    id("org.sonarqube") version "3.3"
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

tasks.named("build") {
    dependsOn(":chattools:build", ":duyuru:build")
}
