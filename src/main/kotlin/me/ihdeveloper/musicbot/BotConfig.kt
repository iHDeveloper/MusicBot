package me.ihdeveloper.musicbot

import kotlinx.serialization.Serializable

@Serializable
data class BotConfig(
    val token: String
)
