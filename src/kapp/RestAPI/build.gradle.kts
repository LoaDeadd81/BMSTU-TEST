val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val slf4j_version: String by project
val allureVersion: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.5"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    application
    id("io.qameta.allure-adapter") version "2.11.2"
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

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-resources")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("org.jetbrains:markdown:0.5.2")
    implementation("io.ktor:ktor-server-double-receive:$ktor_version")
    implementation("io.ktor:ktor-server-call-id:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("com.apurebase:kgraphql-ktor:0.19.0")
//    testImplementation("io.ktor:ktor-server-tests-jvm")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

//    allureRawResultElements(files("${layout.buildDirectory}/allure-results"))
//    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
//    testImplementation("io.qameta.allure:allure-junit5:$allureVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("com.radcortez.flyway:flyway-junit5-extension:1.4.0")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events ("PASSED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR")
    }
    testLogging.showStandardStreams = true

}

//allure{
//    adapter{
//        autoconfigure.set(true)
//        autoconfigureListeners.set(true)
//        aspectjWeaver.set(true)
//        frameworks{
//            junit5{
//                enabled.set(true)
//                autoconfigureListeners.set(true)
//            }
//        }
//    }
//}

allure {
    adapter {
        frameworks {
            junit5
        }
    }
}


kotlin {
    jvmToolchain(20)
}

application {
    mainClass.set("main.MainKt")
}



