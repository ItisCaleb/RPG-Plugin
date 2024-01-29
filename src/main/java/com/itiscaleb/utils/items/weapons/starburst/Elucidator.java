package com.itiscaleb.utils.items.weapons.starburst;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Elucidator extends Starburst_TwinBlade {

    public Elucidator() {
        super(new ItemStack(Material.NETHERITE_SWORD));
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

}
