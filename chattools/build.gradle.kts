plugins {
    java
    kotlin("plugin.lombok") version "2.1.21"
    id("io.freefair.lombok") version "8.13"
    kotlin("jvm") version "1.9.25"
}

group = "aytackydln"
version = "0.1.0"
description = "chattools"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.30"
val jacksonVersion = "2.14.1"

dependencies {
    implementation("org.springframework:spring-context-support:5.3.15")
    implementation("org.projectlombok:lombok:$lombokVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.+")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin") {
    // Configuration if needed
}