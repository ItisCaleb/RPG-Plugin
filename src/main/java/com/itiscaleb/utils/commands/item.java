package com.itiscaleb.utils.commands;

import com.itiscaleb.utils.items.FrostMourne;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class item implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("玩家才能使用此指令");
            return true;
        }
        if(args.length>=2){
            Player p = (Player)sender;
            if(args[0].equalsIgnoreCase("get")){
                if(args[1].equalsIgnoreCase("frostmourne")){
                    p.getInventory().addItem(new FrostMourne().getItem());
                    p.sendMessage(Component.text("霜之哀傷"));
                    return true;
                }
            }else if(args[0].equalsIgnoreCase("set")){
                p.sendMessage("測試中");
            }

        }
        return false;
    }
}
