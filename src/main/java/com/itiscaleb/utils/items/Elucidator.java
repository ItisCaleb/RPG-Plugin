package com.itiscaleb.utils.items;

import com.destroystokyo.paper.ParticleBuilder;
import com.itiscaleb.utils.Utils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Elucidator extends SpecialWeapon{

    public Elucidator() {
        super(new ItemStack(Material.NETHERITE_SWORD));
        addSkillFunc("星爆氣流斬",this::castStarburstStream);
        addSkillFunc("垂直四方斬",this::VerticalSquare);
    }

    boolean VerticalSquare(Player player){
        Location loc = player.getLocation();
        new ParticleBuilder(Particle.SWEEP_ATTACK)
                .location(loc.add(loc.getDirection().setY(0).multiply(2)).add(0,1.5,0))
                .receivers(10)
                .count(10)
                .spawn();
        return true;
    }

    boolean castStarburstStream(Player player){
        ItemMeta off = player.getInventory().getItemInOffHand().getItemMeta();
        if (off==null){
            player.sendMessage("§c你需要逐暗者才能施放這個技能!");
            return false;
        }
        String offType = (String) getPersistentData(off,"weapon-type","String");
        //check main hand and off hand
        if (offType !=null && offType.equals("Dark_Repulser")){
            StarburstStream(player);
            setPersistentData(off,findSkillByName("星爆氣流斬").id+"-cd","Long",System.currentTimeMillis());
            player.sendMessage("§bStarburst Stream!");
            return true;
        }else {
            player.sendMessage("§c你需要逐暗者才能施放這個技能!");
            return false;
        }
    }
    void StarburstStream(Player player){
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
                .offset(5,5,5)
                .color(Color.BLUE)
                .count(300);
        /*
        確認點在半圓內
        令x正向角度為0，A=(|O-A|,θ')
        O為玩家位置，面向角度為θ
        -π/2<θ'-θ<π/2 && |O-A|<r
         */
        BukkitRunnable runnable = new BukkitRunnable(){
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
                for (Entity entity: player.getNearbyEntities(6,2,6)){
                    if(entity instanceof LivingEntity living){
                        Location A = living.getLocation();
                        double r = A.distance(O);
                        Vector u = new Vector(A.getX()-O.getX(),0,A.getZ()-O.getZ());
                        double angle = vec.angle(u);
                        if(r<radius && -Math.PI/2 <= angle && angle <= Math.PI/2){
                            living.damage(5);
                        }
                    }
                }
            }
        };
        runnable.runTaskTimer(Utils.getInstance(),0,3);
    }
}
