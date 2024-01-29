package com.itiscaleb.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();
        switch (command.getName()){
            case "sitem":
                if (args.length==1){
                    list.addAll(Arrays.asList("set", "get","show"));
                }else if (args.length==2 && args[0].equalsIgnoreCase("get")){
                    list.addAll(Configs.getWeaponAttr().getKeys(false).stream().toList());
                }
                break;
            case "utilrule":
                if (args.length==1){
                    list.addAll(Arrays.asList("TNT_explode"));
                }else if (args.length==2){
                    list.addAll(Arrays.asList("true","false"));
                }
                break;
        }
        if(list.isEmpty()) return null;
        return list;
    }
}
