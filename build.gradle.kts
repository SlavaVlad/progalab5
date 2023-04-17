
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    application
}

group = "com.apu"
version = "1.0"

repositories {
    mavenCentral()
}

buildscript {
    repositories { mavenCentral() }

    dependencies {
        val kotlinVersion = "1.8.10"
        kotlin("gradle-plugin", version = kotlinVersion)
        kotlin("serialization", version = kotlinVersion)
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation ("com.charleskorn.kaml:kaml:0.51.0")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}