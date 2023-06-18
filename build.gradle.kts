plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("org.jetbrains.compose") version "1.4.0"
    application
}

group = "com.apu"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
    implementation("io.ktor:ktor-client-logging-jvm:2.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
    implementation("io.ktor:ktor-serialization-gson:2.3.1")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.1")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    val ktor_version = "2.3.1"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    implementation(compose.desktop.currentOs)
}

tasks.test {
    useJUnitPlatform()
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "11"
//}

application {
    mainClass.set("MainKt")
}