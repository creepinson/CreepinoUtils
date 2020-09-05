package dev.throwouterror.bukkit.util

import com.google.common.io.ByteStreams
import dev.throwouterror.bukkit.core.CreepinoUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project CreepinoUtils
 */
object BungeeUtils {
    fun sendPlayerToServer(player: Player, server: String?) {
        try {
            val out = ByteStreams.newDataOutput()
            out.writeUTF("Connect")
            out.writeUTF(server)
            player.sendPluginMessage(CreepinoUtils.instance, "BungeeCord", out.toByteArray())
        } catch (e: Exception) {
            player.sendMessage(ChatColor.RED.toString() + "Error when trying to connect to " + server)
        }
    }
}