package me.ihdeveloper.musicbot.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.ihdeveloper.musicbot.Bot

class ShutdownCommand : Command() {
    init {
        name = "shutdown"
        help = "Shuts down the bot"
        ownerCommand = true
    }

    override fun execute(event: CommandEvent) {
        event.reply("Shutting down...")
        Bot.stop()
    }
}