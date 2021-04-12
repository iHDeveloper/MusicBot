plugins {
    java
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("plugin.serialization") version "1.4.30"
}

group = "me.ihdeveloper"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        name = "m2-dv8tion"
        url = uri("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    implementation("net.dv8tion:JDA:4.2.1_255")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            useIR = true
        }
    }

    jar {
        manifest {
            attributes (
                "Main-Class" to "me.ihdeveloper.musicbot.Main"
            )
        }
    }
}

