package com.itiscaleb;


import com.itiscaleb.utils.Utils;
import com.itiscaleb.utils.items.SpecialWeapon;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir")+"/src/test/resources/weapon.yml";
        Object hi = null;
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(path)));
            //ConfigurationSection c = config.getConfigurationSection("Hammer_of_the_Naaru");
            String bruh = (String) hi;
            System.out.println(bruh);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }



}

