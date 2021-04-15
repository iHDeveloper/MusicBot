package me.ihdeveloper.musicbot

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.VoiceChannel
import java.nio.ByteBuffer
import java.util.logging.Logger

class GuildPlayer (
    id: String
) : AudioEventAdapter(), AudioSendHandler {
    companion object {
        private val logger = Logger.getLogger(GuildPlayer::class.java.name)
    }

    private val guild: Guild = Bot.jda.getGuildById(id) ?: error("Failed to find the guild!")
    private val player: AudioPlayer = Bot.audioPlayerManager.createPlayer()
    private val queue = ArrayDeque<AudioTrack>()

    private var lastFrame: AudioFrame? = null

    fun join(channel: VoiceChannel) {
        logger.info("Joining a channel with name ${channel.name}...")
        guild.audioManager.openAudioConnection(channel)
        guild.audioManager.sendingHandler = this
        player.addListener(this)
    }

    fun play(track: AudioTrack) {
        if (isEmpty()) {
            logger.info("No track is playing currently!")
            logger.info("Playing track: ${track.info.title} (Author: ${track.info.author})")
            player.playTrack(track)
            return
        }

        logger.info("Adding track to the queue: ${track.info.title} (Author: ${track.info.author})")
        queue.add(track)
    }

    fun now() = player.playingTrack

    fun next() {
        if (isEmpty()) {
            logger.info("No track in the queue to play! Stopping the player...")
            player.stopTrack()
            return
        }

        val track = queue.removeFirst()
        logger.info("Next track: ${track.info.title} (Author: ${track.info.author})")
        player.playTrack(track)
    }

    fun kill() {
        guild.audioManager.closeAudioConnection()
        guild.audioManager.sendingHandler = null
        player.removeListener(this)
    }

    override fun canProvide(): Boolean {
        lastFrame = player.provide()
        return lastFrame != null
    }

    override fun provide20MsAudio(): ByteBuffer? {
        return ByteBuffer.wrap(lastFrame!!.data)
    }

    override fun isOpus(): Boolean {
        return true
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack) {
        logger.info("[AudioPlayer#${guild.id}] Playing track ${track.info.title} (Author: ${track.info.author})...")
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack, endReason: AudioTrackEndReason) {
        logger.info("[AudioPlayer#${guild.id}] Tracked Ended! (Reason: ${endReason.name}) Calling next in the queue...")
        next()
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack, exception: FriendlyException) {
        logger.info("[AudioPlayer#${guild.id}] Tracked Crashed! Calling next in the queue...")
        exception.printStackTrace()
        next()
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack, thresholdMs: Long) {
        logger.info("[AudioPlayer#${guild.id}] Tracked is stuck! (Threshold: ${thresholdMs}ms) Calling next in the queue...")
        next()
    }

    fun isInVoiceChannel() = (guild.audioManager.connectedChannel != null)

    fun isEmpty() = queue.isEmpty()

}