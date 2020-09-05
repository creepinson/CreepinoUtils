package dev.throwouterror.bukkit.core.command

import dev.throwouterror.bukkit.core.CreepinoUtils
import me.creepinson.api.util.TextUtils.color
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class AnnounceCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "Not enough arguments! A help page is work in progress.")
            return false
        }

        if (!sender.hasPermission("creepinoutils.admin.announce")) {
            sender.sendMessage(ChatColor.RED.toString() + "You do not have permission to execute this command!")
            return false
        }

        if (args.size >= 2 && args[0] != "" && args[1] != "") {
            val builder = StringBuilder()
            for (i in 1 until args.size) {
                if (args[i] != "") {
                    builder.append(args[i])
                    if (i != args.size - 1) {
                        builder.append(" ")
                    }
                }
            }
            val message = color(builder.toString())
            val prefix = color(if (args[0].startsWith("p=")) args[0] else CreepinoUtils.instance.config.getString("announcer-prefix"))
            sender.server.broadcastMessage("$prefix $message")
            return true
        }
        return false
    }
}