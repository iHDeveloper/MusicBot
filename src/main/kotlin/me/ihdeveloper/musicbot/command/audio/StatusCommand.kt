package me.ihdeveloper.musicbot.command.audio

import com.jagrosh.jdautilities.command.CommandEvent
import me.ihdeveloper.musicbot.Bot
import me.ihdeveloper.musicbot.command.AudioCommand

class StatusCommand : AudioCommand(
    "status",
    "Shows information about current playing track"
) {

    override fun execute(event: CommandEvent) {
        val member = event.member
        val voiceState = member.voiceState!!

        if (!voiceState.inVoiceChannel()) {
            event.replyError("You need to be in a voice channel!")
            return
        }

        val guild = voiceState.guild
        val player = Bot.getGuildPlayer(voiceState.guild.id)

        if (player?.now() == null) {
            event.reply("There's nothing currently playing!")
            return
        }

        val current = player.now().info
        event.reply("Currently Playing: ${current.title} (Author: ${current.author})")
    }

}