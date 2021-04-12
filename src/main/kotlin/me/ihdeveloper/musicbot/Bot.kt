package me.ihdeveloper.musicbot

import com.jagrosh.jdautilities.command.CommandClientBuilder
import me.ihdeveloper.musicbot.command.audio.KillCommand
import me.ihdeveloper.musicbot.command.audio.PlayCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag

object Bot {
    lateinit var jda: JDA
    lateinit var config: BotConfig

    fun init() {
        val builder = JDABuilder.createDefault(config.token).apply {
            setMemberCachePolicy(MemberCachePolicy.VOICE)
            setChunkingFilter(ChunkingFilter.NONE)
            enableCache(CacheFlag.VOICE_STATE)
            enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES)
            disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)
            setLargeThreshold(50)
        }

        val commandBuilder = CommandClientBuilder().apply {
            setPrefix("hd")
            setActivity(null)

            if (config.owner != null) {
                setOwnerId(config.owner)
            }

            addCommand(PlayCommand())
            addCommand(KillCommand())
        }

        builder.addEventListeners(commandBuilder.build())

        jda = builder.build()
    }

    fun run() {
        jda.awaitReady()

        jda.presence.activity = Activity.competing("my mind")

        println("Bot is ready to use!")
    }
}