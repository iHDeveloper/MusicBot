package me.ihdeveloper.musicbot.command.audio

import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.ihdeveloper.musicbot.Bot
import me.ihdeveloper.musicbot.command.AudioCommand

class StreamCommand() : AudioCommand(
    "stream",
    "Plays from an audio stream"
) {
    override fun execute(event: CommandEvent) {
        event.member.voiceState.run {
            if (this == null || !this.inVoiceChannel()) {
                event.replyError("You need to be in a voice channel to use this command!")
                return
            }
        }

        val player = Bot.getGuildPlayer(event.guild.id)

        if (player == null) {
            event.replyError("Failed to load the guild player!")
            return
        }

        if (!player.isInVoiceChannel()) {
            event.replyError("I'm not in the voice channel!")
            return
        }

        println("Loading the audio stream...")
        Bot.audioPlayerManager.loadItem("icy://audio.ihdeveloper.me", object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                println("Audio stream has been loaded!")
                player.play(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                event.replyError("The bot doesn't support playlists for now!")
            }

            override fun noMatches() {
                println("The audio stream doesn't exist :(")
                event.replyError("The bot didn't find what you are looking for!")
            }

            override fun loadFailed(exception: FriendlyException) {
                println("Failed to load the audio stream!")
                event.replyError("The bot failed to find what you are looking for! `Message: ${exception.message}`.\n Contact the owner to check the console!")
                exception.printStackTrace()
            }
        })
    }
}