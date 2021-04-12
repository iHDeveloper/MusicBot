package me.ihdeveloper.musicbot.command

import com.jagrosh.jdautilities.command.Command
import net.dv8tion.jda.api.Permission

abstract class AudioCommand(
    name: String,
    help: String
) : Command() {
    init {
        this.name = name
        this.help = help

        cooldown = 3
        category = Category("audio")
        ownerCommand = true
    }
}