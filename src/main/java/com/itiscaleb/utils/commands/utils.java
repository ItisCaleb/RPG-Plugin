package com.itiscaleb.utils.commands;

import com.itiscaleb.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;



public class utils implements CommandExecutor {
    static Utils utils = Utils.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length>=2){
            if(args[0].equals("TNT_explode")){
                if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
                    TNTExplode(sender,args[1]);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    void TNTExplode(CommandSender s, String bool){
        utils.getConfig().set("rules.TNT_explode",Boolean.parseBoolean(bool));
        utils.saveConfig();
        s.sendMessage("TNT_explode set to "+Boolean.parseBoolean(bool));
    }
}
