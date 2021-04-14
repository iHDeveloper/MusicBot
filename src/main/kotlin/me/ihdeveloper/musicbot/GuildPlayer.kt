package me.ihdeveloper.musicbot

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.VoiceChannel
import java.util.logging.Logger

class GuildPlayer(
    id: String
) {
    companion object {
        private val logger = Logger.getLogger(GuildPlayer::class.java.name)
    }

    private val guild: Guild = Bot.jda.getGuildById(id) ?: error("Failed to find the guild!")

    private var player: AudioPlayer? = null

    fun join(channel: VoiceChannel) {
        logger.info("Joining a channel with name ${channel.name}...")
        guild.audioManager.openAudioConnection(channel)
    }

}