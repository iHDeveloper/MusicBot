@file:JvmName("Main")

package me.ihdeveloper.musicbot

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    println("Initializing the music bot... By @iHDeveloper")
    val configFile = File("config.json")

    if (!configFile.exists()) {
        error("Failed to find config.json in the working directory!")
    }

    Bot.run {
        config = Json.decodeFromString(configFile.readText())
        init()
        run()
    }
}
