package com.itiscaleb.utils;

import com.itiscaleb.utils.commands.item;
import com.itiscaleb.utils.commands.title;
import com.itiscaleb.utils.commands.utils;
import com.itiscaleb.utils.events.ExplodeEvent;
import com.itiscaleb.utils.events.JoinEvent;
import com.itiscaleb.utils.events.SpecialItemEvents;
import com.itiscaleb.utils.items.FrostMourne;
import com.itiscaleb.utils.items.Hammer_of_the_Naaru;
import com.itiscaleb.utils.items.SpecialWeapon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class Utils extends JavaPlugin {

    static Utils Instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        try {
            Configs.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadEvents();
        loadCommands();
        loadTabComplete();
        loadSpecialWeapons();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadEvents(){
        getServer().getPluginManager().registerEvents(new ExplodeEvent(),this);
        getServer().getPluginManager().registerEvents(new JoinEvent(),this);
        getServer().getPluginManager().registerEvents(new SpecialItemEvents(),this);
    }

    public void loadCommands(){
        getCommand("utilrule").setExecutor(new utils());
        getCommand("ptitle").setExecutor(new title());
        getCommand("sitem").setExecutor(new item());
    }

    public void loadTabComplete(){
        getCommand("utilrule").setTabCompleter(new TabComplete());
        getCommand("sitem").setTabCompleter(new TabComplete());
    }

    public void loadSpecialWeapons(){
        SpecialWeapon.registerWeapon(new FrostMourne(new ItemStack(Material.NETHERITE_SWORD)));
        SpecialWeapon.registerWeapon(new Hammer_of_the_Naaru(new ItemStack(Material.DIAMOND_AXE)));
    }

    public static Utils getInstance() {
        return Instance;
    }

    public static void Log(String text){
        Instance.getLogger().info(text);
    }
}
