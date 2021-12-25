package com.itiscaleb.utils.items;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;


public class Hammer_of_the_Naaru extends SpecialWeapon {

    public Hammer_of_the_Naaru(ItemStack item) {
        super(item);
        addSkillFunc("黎明曙光",this::heal);
        addSkillFunc("聖盾術",this::holyShield);
    }


    private void heal(Player player) {
        Location loc = player.getLocation();
        new ParticleBuilder(Particle.GLOW)
                .location(loc)
                .count(1000)
                .offset(7, 0.5, 7)
                .spawn();
        ArrayList<Player> plist = (ArrayList<Player>) loc.getNearbyPlayers(7);
        for (Player p : plist) {
            p.sendMessage(Component.text("§e§l你感受到聖光流淌在你的身軀中"));
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,2,4,false));
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,50,10);
        }
    }

    private void holyShield(Player player){
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*20,5,false));
        player.sendMessage(Component.text("§e§l你的意志如千顆太陽般閃耀，沒有人能撼動你"));
    }


    @Override
    public void killEvent(LivingEntity e, LivingEntity killer, ItemStack stack) {}
}
