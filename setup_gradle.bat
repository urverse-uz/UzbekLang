@echo off
echo Creating build.gradle.kts...

echo.> build.gradle.kts
echo import org.jetbrains.kotlin.gradle.tasks.KotlinCompile>> build.gradle.kts
echo.>> build.gradle.kts
echo plugins {>> build.gradle.kts
echo     kotlin("jvm") version "1.8.0">> build.gradle.kts
echo     application>> build.gradle.kts
echo }>> build.gradle.kts
echo.>> build.gradle.kts
echo repositories {>> build.gradle.kts
echo     mavenCentral()>> build.gradle.kts
echo }>> build.gradle.kts
echo.>> build.gradle.kts
echo dependencies {>> build.gradle.kts
echo     implementation(kotlin("stdlib"))>> build.gradle.kts
echo }>> build.gradle.kts
echo.>> build.gradle.kts
echo application {>> build.gradle.kts
echo     mainClass.set("MainKt")>> build.gradle.kts
echo }>> build.gradle.kts
echo.>> build.gradle.kts
echo tasks.withType<KotlinCompile> {>> build.gradle.kts
echo     kotlinOptions.jvmTarget = "1.8">> build.gradle.kts
echo }>> build.gradle.kts

echo Creating settings.gradle.kts...

echo.> settings.gradle.kts
echo rootProject.name = "UzbekLang">> settings.gradle.kts

echo Gradle setup files have been created.
