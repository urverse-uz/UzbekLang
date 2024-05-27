import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin.srcDirs("src")
    }
}

dependencies {
    implementation(kotlin("stdlib")) // Include Kotlin standard library
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.jar {
    from(sourceSets.main.get().output)
    manifest {
        attributes(
            "Main-Class" to "MainKt"
        )
    }
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}
