package com.itiscaleb;


import com.itiscaleb.utils.Utils;
import com.itiscaleb.utils.items.SpecialWeapon;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir")+"/src/test/resources/weapon.yml";
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(path)));
            ConfigurationSection c = config.getConfigurationSection("Hammer_of_the_Naaru");
            List<Map<?,?>> skill = c.getMapList("skills");
            int i = 0;
            //use index to track individual cool down
            for (Map<?,?> map: skill) {
                String name = (String) map.get("name");
                System.out.println(map.get("cd"));
                i++;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

}

