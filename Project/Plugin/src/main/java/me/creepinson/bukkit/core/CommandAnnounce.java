package me.creepinson.bukkit.core;

import me.creepinson.utils.TextUtils;
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
        if(args.length > 2){
            sender.sendMessage(ChatColor.RED + "Too many arguments!");
            return false;
        }
        if(args.length == 2 && args[0] != null && args[0] != "" && args[1] != null && args[1] != ""){
            String prefix = TextUtils.withColor(args[0]);
            String message = TextUtils.withColor(args[1]);
            sender.getServer().broadcastMessage(prefix + " " + message);
            return true;
        } else if(args.length == 1 && args[0] != null && args[0] != ""){
            if(!CreepinoUtils.getInstance().getConfig().contains("announcer-prefix")){
                sender.sendMessage(ChatColor.RED + "The CreepinoUtils configuration does not contain an announcer prefix! Either fix the configuration file, or add the prefix before the message in this command.");
                return false;
            }
            String prefix = TextUtils.withColor(CreepinoUtils.getInstance().getConfig().getString("announcer-prefix"));
            String message = TextUtils.withColor(args[0]);
            sender.getServer().broadcastMessage(prefix + " " + message);
            return true;
        }
        return false;
    }

}
