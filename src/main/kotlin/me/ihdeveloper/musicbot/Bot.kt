package me.ihdeveloper.musicbot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy

object Bot {
    lateinit var jda: JDA
    lateinit var config: BotConfig

    fun init() {
        val builder = JDABuilder.createDefault(config.token)

        builder.setMemberCachePolicy(MemberCachePolicy.NONE)
        builder.setChunkingFilter(ChunkingFilter.NONE)
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
        builder.setLargeThreshold(50)
        builder.setActivity(Activity.competing("my mind"))

        jda = builder.build()
    }

    fun run() {
        println("Starting JDA...")

        jda.awaitReady()

        println("Bot is ready to use!")
    }
}