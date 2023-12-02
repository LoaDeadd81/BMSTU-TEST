val logback_version: String by project
val slf4j_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val allureVersion: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    application
    java
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    kotlin("plugin.serialization") version "1.9.10"
    id("io.qameta.allure-aggregate-report") version "2.11.2"
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
    implementation(project(":integro"))

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-nop:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    implementation("com.zaxxer:HikariCP:4.0.3")

//    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
//    testImplementation("io.qameta.allure:allure-junit5:$allureVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("com.radcortez.flyway:flyway-junit5-extension:1.4.0")


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
