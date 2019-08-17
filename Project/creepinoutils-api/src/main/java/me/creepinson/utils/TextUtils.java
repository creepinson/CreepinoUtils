package me.creepinson.utils;

import org.bukkit.ChatColor;

public class TextUtils {

    private static String DEFAULT_JOIN_MESSAGE = "has logged in.";
    private static String DEFAULT_LEAVE_MESSAGE = "has left.";


    public static String getDefaultJoinMessage(String playerName) {
    	return ChatColor.YELLOW + playerName + " " + DEFAULT_JOIN_MESSAGE;
    }

    public static String getDefaultLeaveMessage(String playerName) {
    	return ChatColor.YELLOW + playerName + " " + DEFAULT_LEAVE_MESSAGE;
    }

    public static String withColor(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
