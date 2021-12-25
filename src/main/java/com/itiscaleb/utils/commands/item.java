package com.itiscaleb.utils.commands;

import com.itiscaleb.utils.items.FrostMourne;
import com.itiscaleb.utils.items.Hammer_of_the_Naaru;
import com.itiscaleb.utils.items.SpecialWeapon;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

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
