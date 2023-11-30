val logback_version: String by project
val slf4j_version: String by project


buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.6.0")
    }
}

plugins {
    kotlin("jvm") version "1.9.0"
    id("org.flywaydb.flyway") version "10.0.0"
    id("io.qameta.allure") version "2.11.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



dependencies {
//    testImplementation(kotlin("test"))
    implementation(project(":BL"))

//    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation("org.jetbrains.exposed:exposed-core:0.44.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.44.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-nop:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    testImplementation("com.radcortez.flyway:flyway-junit5-extension:1.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")

    implementation("io.qameta.allure:allure-bom:2.24.0")
    testImplementation("io.qameta.allure:allure-junit5:2.24.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events ("PASSED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR")
    }
    testLogging.showStandardStreams = true
}

kotlin {
    jvmToolchain(20)
}

flyway {
    url = "jdbc:postgresql://localhost:65432/back"
    user = "main_user"
    password = "main_user"
    locations = arrayOf("filesystem:src/main/resources/db/migration/")
    cleanDisabled = false
}



