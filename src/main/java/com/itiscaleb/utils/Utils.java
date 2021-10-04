package com.itiscaleb.utils;

import com.itiscaleb.utils.commands.item;
import com.itiscaleb.utils.commands.title;
import com.itiscaleb.utils.commands.utils;
import com.itiscaleb.utils.events.ExplodeEvent;
import com.itiscaleb.utils.events.JoinEvent;
import com.itiscaleb.utils.events.SpecialItemEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class Utils extends JavaPlugin {

    static Utils Instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        Configs.loadConfig();
        loadEvents();
        loadCommands();
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



    public static Utils getInstance() {
        return Instance;
    }
}
