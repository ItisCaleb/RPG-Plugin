package com.itiscaleb.utils.commands;

import com.itiscaleb.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class title implements CommandExecutor {
    static Utils utils = Utils.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length >= 2){
            String player = args[0];

            if(args[1].equals("clear")){
                utils.getConfig().set("titles."+player,null);
                utils.saveConfig();
                sender.sendMessage("已將"+player+"的稱號清除");
                if(utils.getServer().getPlayer(player)!=null){
                    utils.getServer().getPlayer(player).displayName(Component.text(player));
                }
                return true;
            }
            utils.getConfig().set("titles."+args[0],args[1]);
            utils.saveConfig();
            String title = ChatColor.translateAlternateColorCodes('&',args[1]);
            sender.sendMessage("已將"+player+"的稱號改為"+title);
            if(utils.getServer().getPlayer(player)!=null){
                utils.getServer().getPlayer(player).displayName(Component.text(title+ChatColor.RESET+" "+player));
            }
            return true;
        }
        return false;
    }
}
