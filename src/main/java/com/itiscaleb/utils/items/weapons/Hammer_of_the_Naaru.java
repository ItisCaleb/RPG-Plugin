package com.itiscaleb.utils.items.weapons;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;


public class Hammer_of_the_Naaru extends SpecialWeapon {

    public Hammer_of_the_Naaru() {
        super(new ItemStack(Material.DIAMOND_AXE));
        addSkillFunc("黎明曙光",this::healRange);
        addSkillFunc("治療禱言",this::heal);
        addSkillFunc("聖盾術",this::holyShield);
    }


    boolean healRange(Player player) {
        Location loc = player.getLocation();
        new ParticleBuilder(Particle.GLOW)
                .location(loc)
                .count(1000)
                .receivers(10)
                .offset(7, 0.5, 7)
                .spawn();
        ArrayList<Player> plist = (ArrayList<Player>) loc.getNearbyPlayers(7);
        for (Player p : plist) {
            p.sendMessage(Component.text("§e§l你感受到聖光流淌在你的身軀中"));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20,4,false));
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,50,10);
        }
        return true;
    }
    
    boolean heal(Player player){
        Location loc = player.getLocation();
        new ParticleBuilder(Particle.GLOW)
                .location(loc)
                .count(200)
                .receivers(4)
                .offset(1, 0.5, 1)
                .spawn();
        player.sendMessage(Component.text("§e§l在聖光之中，我們合而為一"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20,6,false));
        player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,50,10);
        return true;
    }

    boolean holyShield(Player player){
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*20,5,false));
        player.sendMessage(Component.text("§e§l你的意志如千顆太陽般閃耀，沒有人能撼動你"));
        return true;
    }

}
