val logback_version: String by project
val slf4j_version: String by project
val ktor_version: String by project
val kotlin_version: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    application
    java
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    kotlin("plugin.serialization") version "1.9.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":BL"))
    implementation(project(":DA"))
    implementation(project(":RestAPI"))

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-nop:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    implementation("com.zaxxer:HikariCP:4.0.3")

    implementation("io.qameta.allure:allure-bom:2.24.0")
    testImplementation("io.qameta.allure:allure-junit5:2.24.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("com.radcortez.flyway:flyway-junit5-extension:1.4.0")

    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
}

tasks.test {
    useJUnitPlatform()

    if (project.hasProperty("excludeTests")) {
        exclude(project.property("excludeTests").toString())
    }
}

kotlin {
    jvmToolchain(20)

}

application {
    mainClass.set("main.MainKt")
}

val mainClassName = "main.MainKt"

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = mainClassName
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}
