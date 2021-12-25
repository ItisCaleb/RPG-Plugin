package com.itiscaleb.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Configs {
    static Utils utils = Utils.getInstance();
    public static void loadConfig() throws IOException {
        FileConfiguration config = utils.getConfig();
        config.addDefault("rules.TNT_explode",true);
        config.addDefault("titles","");
        config.options().copyDefaults(true);
        utils.saveConfig();
    }

    public static FileConfiguration getWeaponAttr(){
        return YamlConfiguration.loadConfiguration(new InputStreamReader(utils.getResource("weapon.yml")));
    }

}
