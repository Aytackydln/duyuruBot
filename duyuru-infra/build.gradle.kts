plugins {
    java
    id("io.freefair.lombok") version "8.7.1"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.hibernate.orm") version "6.3.1.Final"
    war
}

val mapstructVersion = "1.6.3"

group = "aytackydln"
version = "1.8.1"
description = "Duyuru botu"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":duyuru"))
    implementation(project(":chattools"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    compileOnly("org.apache.tomcat.embed:tomcat-embed-jasper")
    implementation("org.jsoup:jsoup:1.15.3")

    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl")

    // add org.jetbrains.kotlin.jvm
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    compileOnly("org.projectlombok:lombok")
    compileOnly("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("mysql:mysql-connector-java:8.0.33")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Configure the Hibernate plugin
hibernate {
    enhancement {
        enableLazyInitialization = false
        enableDirtyTracking = false
        enableAssociationManagement = false
    }
}

tasks.test {
    useJUnitPlatform()
}
