plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.jprofiler") version "14.0-PRE1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.ktorm:ktorm-core:3.6.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}