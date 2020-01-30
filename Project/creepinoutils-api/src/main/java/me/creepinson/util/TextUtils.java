package me.creepinson.util;

import org.bukkit.ChatColor;

public class TextUtils {

    private static String DEFAULT_JOIN_MESSAGE = "has joined the game.";
    private static String DEFAULT_LEAVE_MESSAGE = "has left the game.";

    public static String getJoinMessage(String playerName) {
        return ChatColor.YELLOW + playerName + " " + DEFAULT_JOIN_MESSAGE;
    }

    public static String getLeaveMessage(String playerName) {
        return ChatColor.YELLOW + playerName + " " + DEFAULT_LEAVE_MESSAGE;
    }

    /**
     * Color-codes a string containing color codes formatted with an '&' symbol.
     * @param input A string to color-code.
     * @return Colorized String
     */
    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
