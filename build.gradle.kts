
import org.gradle.internal.impldep.org.bouncycastle.asn1.iana.IANAObjectIdentifiers.experimental
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
        kotlin("kotlin-parcelize-runtime")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin.plugin.parcelize:org.jetbrains.kotlin.plugin.parcelize.gradle.plugin:1.8.10")
    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.8.20-RC")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("org.jetbrains.kotlin:kotlin-test:1.8.20-RC")
    implementation("org.jetbrains.kotlin:kotlin-test-junit5:1.8.20-RC")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.20-RC")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation ("com.charleskorn.kaml:kaml:0.51.0")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
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