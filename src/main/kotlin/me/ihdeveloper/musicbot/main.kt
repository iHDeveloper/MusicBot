@file:JvmName("Main")

package me.ihdeveloper.musicbot

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    val configFile = File("config.json")

    if (!configFile.exists()) {
        error("Failed to find config.json in the working directory!")
    }

    Bot.run {
        config = Json.decodeFromString(configFile.readText())

        println("Initializing Bot... By @iHDeveloper")
        init()

        println("Starting Bot...")
        start()
        println("Bot is ready to use!")
    }
}
