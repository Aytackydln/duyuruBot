plugins {
        java
        id("io.freefair.lombok") version "6.2.0"
        id("org.springframework.boot") version "2.7.18"
        id("io.spring.dependency-management") version "1.0.11.RELEASE"
        `maven-publish`
        war
    }

    val mapstructVersion = "1.5.0.Beta2"

    repositories {
        maven { url = uri("https://repo.spring.io/release") }
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
        implementation("javax.servlet:jstl")
        implementation("org.jsoup:jsoup:1.15.3")

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

    tasks.test {
        useJUnitPlatform()
    }

    group = "aytackydln"
    version = "1.8.0"
    description = "Duyuru botu"
    java.sourceCompatibility = JavaVersion.VERSION_17

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs = listOf("-Amapstruct.defaultComponentModel=spring")
    }

    tasks.named<org.springframework.boot.gradle.tasks.bundling.BootWar>("bootWar") {
        isEnabled = true
        archiveFileName.set("duyuru.war")
        // webInf { from("src/main/webapp/WEB-INF") }
    }