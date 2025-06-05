plugins {
    java
    id("io.freefair.lombok") version "6.2.0"
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") // version can be specified if needed
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context-support:5.3.15")
    implementation("org.projectlombok:lombok:1.18.22")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

group = "aytackydln"
version = "0.1.0"
description = "chattools"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin") {
    // Configuration if needed
}