plugins {
    java
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "4.0.5"
}

group = "com.sleekydz86"
version = "0.0.1-SNAPSHOT"
description = "market-api"

allprojects {
    group = "com.sleekydz86"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.asciidoctor.jvm.convert")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        compileOnly("org.projectlombok:lombok")

        runtimeOnly("com.h2database:h2")
        runtimeOnly("com.mysql:mysql-connector-j")
        implementation("org.springframework.boot:spring-boot-starter-data-redis")

        implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")

        implementation("org.flywaydb:flyway-core")
        implementation("org.flywaydb:flyway-mysql")

        annotationProcessor("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.rest-assured:rest-assured:5.3.0")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

        implementation("org.springframework.boot:spring-boot-starter-actuator")
        runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    }

    tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = false
    }

    tasks.named<Jar>("jar") {
        enabled = true
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}
