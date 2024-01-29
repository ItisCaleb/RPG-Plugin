package com.itiscaleb.utils.items.weapons.starburst;

import com.destroystokyo.paper.ParticleBuilder;
import com.itiscaleb.utils.Utils;
import com.itiscaleb.utils.items.weapons.SpecialWeapon;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class Starburst_TwinBlade extends SpecialWeapon {

    static ArrayList<String> swords = new ArrayList<>(Arrays.asList("Dark_Repulser", "Elucidator")) ;

    public Starburst_TwinBlade(ItemStack item) {
        super(item);
        addSkillFunc("星爆氣流斬",this::castStarburstStream);
    }

    boolean castStarburstStream(Player player){
        ItemMeta off = player.getInventory().getItemInOffHand().getItemMeta();
        if (off==null){
            player.sendMessage("§c你需要另一把星爆雙刀才能施放這個技能!");
            return false;
        }
        String offType = (String) getPersistentData(off,"weapon-type","String");
        //check main hand and off hand
        if (offType !=null && swords.contains(offType)){
            SpecialWeapon offWeapon = SpecialWeapon.getWeapon(offType);
            setPersistentData(off,offWeapon.findSkillByName("星爆氣流斬").id+"-cd","Long",System.currentTimeMillis());
            player.getInventory().getItemInOffHand().setItemMeta(off);
            double mainAtk = this.getAttribute("atk");
            double offAtk = offWeapon.getAttribute("atk");
            StarburstStream(player,(mainAtk+offAtk)/3);
            player.chat("§bStarburst Stream!");
            return true;
        }else {
            player.sendMessage("§c你需要另一把星爆雙刀才能施放這個技能!");
            return false;
        }
    }
    void StarburstStream(Player player,double atk){
        double radius = 4;
        Location O = player.getLocation();
        Vector vec = O.getDirection().setY(0).multiply(radius/2);
        ParticleBuilder sweep = new ParticleBuilder(Particle.SWEEP_ATTACK)
                .location(O.clone().add(vec).add(0,1,0))
                .receivers(10)
                .offset(1,1.5,1)
                .count(10);
        ParticleBuilder particle = new ParticleBuilder(Particle.REDSTONE)
                .location(O.clone().add(vec).add(0,1,0))
                .receivers(10)
                .offset(4,1.5,4)
                .color(Color.BLUE)
                .count(300);
        /*
        確認點在半圓內
        令x正向角度為0，A=(|O-A|,θ')
        O為玩家位置，面向角度為θ
        -π/2<θ'-θ<π/2 && |O-A|<r
         */
        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                count++;
                if(count > 16){
                    this.cancel();
                    return;
                }
                player.sendMessage("§e"+count+"連擊!");
                sweep.spawn();
                particle.spawn();
                for (Entity entity: O.getNearbyEntities(5,2,5)){
                    if(entity instanceof LivingEntity living){
                        Location A = living.getLocation();
                        double r = A.distance(O);
                        Vector u = new Vector(A.getX()-O.getX(),0,A.getZ()-O.getZ());
                        double angle = vec.angle(u);
                        if(r<radius && -Math.PI/2 <= angle && angle <= Math.PI/2){
                            living.setNoDamageTicks(0);
                            living.damage(atk);
                            living.setKiller(player);
                            living.setNoDamageTicks(10);
                        }
                    }
                }
            }
        }.runTaskTimer(Utils.getInstance(),0,4);
    }
}
