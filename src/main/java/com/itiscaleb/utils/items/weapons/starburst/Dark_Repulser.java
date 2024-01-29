package com.itiscaleb.utils.items.weapons.starburst;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Dark_Repulser extends Starburst_TwinBlade{
    public Dark_Repulser() {
        super(new ItemStack(Material.DIAMOND_SWORD));
        addSkillFunc("音速衝刺",this::SonicLeap);
    }


    boolean SonicLeap(Player player){
        Vector vector = player.getLocation().getDirection().multiply(3).setY(0.5);
        player.setVelocity(vector);
        return true;
    }

}
