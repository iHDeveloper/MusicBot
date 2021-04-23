package me.ihdeveloper.musicbot.command.audio

import com.jagrosh.jdautilities.command.CommandEvent
import me.ihdeveloper.musicbot.Bot
import me.ihdeveloper.musicbot.command.AudioCommand

class StopCommand : AudioCommand(
    "stop",
    "Stops the current audio track"
) {

    override fun execute(event: CommandEvent) {
        val member = event.member
        val voiceState = member.voiceState!!

        if (!voiceState.inVoiceChannel()) {
            event.replyError("You need to be in a voice channel!")
            return
        }

        val currentBotChannel = voiceState.guild.audioManager.connectedChannel
        val player = Bot.getGuildPlayer(voiceState.guild.id)

        if (currentBotChannel == null || player == null) {
            event.replyError("The bot is not in a voice channel!")
            return
        }

        if (voiceState.channel!!.id != currentBotChannel.id) {
            event.replyError("You need to be in the same channel as the bot!")
            return
        }

        player.pause();
    }

}