package me.ihdeveloper.musicbot

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import me.ihdeveloper.musicbot.audio.AudioTrackScheduler
import me.ihdeveloper.musicbot.command.ShutdownCommand
import me.ihdeveloper.musicbot.command.audio.KillCommand
import me.ihdeveloper.musicbot.command.audio.PlayCommand
import me.ihdeveloper.musicbot.command.audio.StatusCommand
import me.ihdeveloper.musicbot.command.audio.StopCommand
import me.ihdeveloper.musicbot.command.audio.StreamCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag

object Bot {
    lateinit var config: BotConfig
    lateinit var jda: JDA
    lateinit var audioPlayerManager: AudioPlayerManager

    private val guildPlayers = mutableMapOf<String, GuildPlayer>()

    private val commands = arrayOf(
        PlayCommand(),
        StopCommand(),
        StreamCommand(),
        StatusCommand(),
        KillCommand(),
        ShutdownCommand()
    )

    fun init() {
        println("Initializing audio player manager...")
        audioPlayerManager = DefaultAudioPlayerManager()
        AudioSourceManagers.registerRemoteSources(audioPlayerManager)

        println("Initializing JDA...")
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

            commands.forEach { addCommand(it) }
        }

        builder.addEventListeners(commandBuilder.build())

        jda = builder.build()
    }

    fun createGuildPlayer(guildId: String): GuildPlayer {
        return guildPlayers.getOrPut(guildId, {
            GuildPlayer(guildId)
        })
    }

    fun getGuildPlayer(guildId: String): GuildPlayer? = guildPlayers[guildId]

    fun deleteGuildPlayer(guildId: String) {
        val player = getGuildPlayer(guildId) ?: return
        player.kill()

        guildPlayers.remove(guildId)
    }

    fun start() {
        jda.awaitReady()

        jda.presence.activity = Activity.competing("my mind")
    }

    fun stop() {
        jda.shutdown()
    }
}