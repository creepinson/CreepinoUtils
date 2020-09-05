package me.creepinson.api.util

import org.bukkit.ChatColor

object TextUtils {
    private const val DEFAULT_JOIN_MESSAGE = "has joined the game."
    private const val DEFAULT_LEAVE_MESSAGE = "has left the game."
    fun getJoinMessage(playerName: String): String {
        return ChatColor.YELLOW.toString() + playerName + " " + DEFAULT_JOIN_MESSAGE
    }

    fun getLeaveMessage(playerName: String): String {
        return ChatColor.YELLOW.toString() + playerName + " " + DEFAULT_LEAVE_MESSAGE
    }

    /**
     * Color-codes a string containing color codes formatted with an '&' symbol.
     * @param input A string to color-code.
     * @return Colorized String
     */
    @JvmStatic
    fun color(input: String?): String {
        return ChatColor.translateAlternateColorCodes('&', input!!)
    }
}