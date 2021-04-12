package me.ihdeveloper.musicbot.command.audio

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.ihdeveloper.musicbot.Bot
import me.ihdeveloper.musicbot.audio.AudioPlayerSendHandler
import me.ihdeveloper.musicbot.command.AudioCommand
import net.dv8tion.jda.api.Permission

class PlayCommand : AudioCommand(
    "play",
    "Plays an audio from a stream"
) {
    init {
        name = "play"
        ownerCommand = true
    }

    override fun execute(event: CommandEvent) {
        val member = event.member
        val voiceState = member.voiceState!!

        if (!voiceState.inVoiceChannel()) {
            event.replyError("You need to be in a voice channel!")
            return
        }

        val channel = voiceState.channel

        with (voiceState) {
            guild.audioManager.openAudioConnection(channel)

            val player = Bot.createAudioPlayer(guild.id)
            guild.audioManager.sendingHandler = AudioPlayerSendHandler(player)

            Bot.audioPlayerManager.loadItem("EXAMPLE", object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    println("[LoadItem] Track Loaded: ${track.info.title}")
                    player.playTrack(track)
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    println("[LoadItem] Playlist Loaded: ${playlist.name}")
                }

                override fun noMatches() {
                    println("[LoadItem] No matches found!")
                }

                override fun loadFailed(exception: FriendlyException) {
                    println("[LoadItem] Failed!")
                    exception.printStackTrace()
                }

            })
        }

        event.message.addReaction("👍").queue()
    }
}