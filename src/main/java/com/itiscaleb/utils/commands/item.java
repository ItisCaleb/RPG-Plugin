package com.itiscaleb.utils.commands;

import com.itiscaleb.utils.items.weapons.SpecialWeapon;
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
        if(args[0].equalsIgnoreCase("show")){
            Player p = (Player)sender;
            p.getServer().sendMessage(p.getInventory().getItemInMainHand().displayName());
            return true;
        }
        if(args.length>=2){
            Player p = (Player)sender;
            if(args[0].equalsIgnoreCase("get")){
                SpecialWeapon stack = SpecialWeapon.getWeapon(args[1]);
                if(stack==null) {
                    p.sendMessage("沒有這把武器");
                    return false;
                }else {
                    p.getInventory().addItem(stack.getItem());
                    p.sendMessage(stack.getItem().displayName());
                }
                return true;
            }else if(args[0].equalsIgnoreCase("set")){
                p.sendMessage("測試中");
            }

        }
        return false;
    }
}
