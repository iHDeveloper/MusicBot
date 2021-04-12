package me.ihdeveloper.musicbot.command.audio

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
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
        }

        event.message.addReaction("👍").queue()
    }
}