package me.ihdeveloper.musicbot.command.audio

import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.ihdeveloper.musicbot.Bot
import me.ihdeveloper.musicbot.command.AudioCommand

class PlayCommand : AudioCommand(
    "play",
    "Plays an audio from a stream"
) {
    init {
        name = "play"
        ownerCommand = true
        arguments = "<identifier>"
    }

    override fun execute(event: CommandEvent) {
        val member = event.member
        val voiceState = member.voiceState!!
        val channel = voiceState.channel

        if (!voiceState.inVoiceChannel()) {
            event.replyError("You need to be in a voice channel!")
            return
        }

        with (voiceState) {
            val player = Bot.createGuildPlayer(guild.id)
            player.join(channel!!)

            Bot.audioPlayerManager.loadItem(event.args, object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    println("[LoadItem] Track Loaded: ${track.info.title}")
                    player.play(track)
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    println("[LoadItem] Playlist Loaded: ${playlist.name}")
                    event.replyError("The bot doesn't support playlists for now!")
                }

                override fun noMatches() {
                    println("[LoadItem] No matches found!")
                    event.replyError("The bot didn't find what you are looking for!")
                }

                override fun loadFailed(exception: FriendlyException) {
                    println("[LoadItem] Failed!")
                    event.replyError("The bot failed to find what you are looking for! `Message: ${exception.message}`.\n Contact the owner to check the console!")
                    exception.printStackTrace()
                }

            })
        }

        event.message.addReaction("👍").queue()
    }
}