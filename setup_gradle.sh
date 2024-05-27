#!/bin/bash

# Create the build.gradle.kts file
cat <<EOL > build.gradle.kts
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("MainKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
EOL

# Create the settings.gradle.kts file
cat <<EOL > settings.gradle.kts
rootProject.name = "UzbekLang"
EOL

echo "Gradle setup files have been created."
