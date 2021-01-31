import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "ru.altmanea"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.litote.kmongo:kmongo:4.2.3")
    implementation("org.litote.kmongo:kmongo-async:4.2.3")
    implementation("org.litote.kmongo:kmongo-coroutine:4.2.3")
    implementation("org.litote.kmongo:kmongo-reactor:4.2.3")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-log4j12:1.7.25")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}