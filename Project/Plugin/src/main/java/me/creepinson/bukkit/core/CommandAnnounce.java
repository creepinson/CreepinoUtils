package me.creepinson.bukkit.core;

import me.creepinson.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Made By Creepinson
 */
public class CommandAnnounce implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage(ChatColor.RED + "Not enough arguments!");
            return false;
        }

        if(args.length >= 2 && args[0] != null && args[0] != "" && args[1] != null && args[1] != ""){
            StringBuilder builder = new StringBuilder();
            for(int i = 1; i <= args.length - 1; i++) {
                if(args[i] != null && args[i] != ""){
                    builder.append(args[i]);
                    if(i != args.length - 1){
                        builder.append(" ");
                    }
                }
            }
            String message = TextUtils.withColor(builder.toString());
            String prefix = TextUtils.withColor(args[0]);

            sender.getServer().broadcastMessage(prefix + " " + message);
            return true;
        }
        return false;
    }

}
