plugins {
    java
    id("io.freefair.lombok") version "6.2.0"
}

repositories {
    maven { url = uri("https://repo.spring.io/release") }
    mavenCentral()
}

dependencies {
    implementation(project(":chattools"))
    compileOnly("org.projectlombok:lombok")
    compileOnly("org.slf4j:slf4j-jdk14:1.7.5")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.test {
    useJUnitPlatform()
}

group = "aytackydln"
version = "1.8.0"
description = "Duyuru botu"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}