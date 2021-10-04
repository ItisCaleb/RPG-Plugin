package com.itiscaleb.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class Configs {
    static Utils utils = Utils.getInstance();
    public static void loadConfig(){
        FileConfiguration config = utils.getConfig();
        config.addDefault("rules.TNT_explode",true);
        config.addDefault("titles","");
        config.options().copyDefaults(true);
        utils.saveConfig();
    }
}
