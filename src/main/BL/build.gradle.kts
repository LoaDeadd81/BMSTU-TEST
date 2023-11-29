val logback_version: String by project
val slf4j_version: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.qameta.allure") version "2.11.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    testImplementation(kotlin("test"))

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-nop:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")

    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")

    implementation("io.qameta.allure:allure-bom:2.24.0")
    testImplementation("io.qameta.allure:allure-junit5:2.24.0")
}

tasks.test {
    useJUnitPlatform()
    useJUnitPlatform()
    testLogging {
        events ("PASSED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR")
    }
    testLogging.showStandardStreams = true

    if (project.hasProperty("excludeTests")) {
        exclude(project.property("excludeTests").toString())
    }
}

kotlin {
    jvmToolchain(20)
}