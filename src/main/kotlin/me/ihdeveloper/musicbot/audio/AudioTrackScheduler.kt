package me.ihdeveloper.musicbot.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason

class AudioTrackScheduler : AudioEventAdapter() {
    override fun onPlayerResume(player: AudioPlayer?) {
        println("Player resumed!")
    }

    override fun onPlayerPause(player: AudioPlayer?) {
        println("Player paused!")
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack) {
        println("Track Start: ${track.info.title}")
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack, exception: FriendlyException) {
        println("Track Exception: ${track.info.title}")
        exception.printStackTrace()
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack, endReason: AudioTrackEndReason) {
        println("Track End: ${track.info.title}, End Reason: $endReason")
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack, thresholdMs: Long) {
        println("Track Stuck: ${track.info.title}, Threshold(ms): $thresholdMs")
    }
}