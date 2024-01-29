package com.itiscaleb.utils.items.weapons.starburst;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Blue_Rose extends Starburst_TwinBlade{

    public Blue_Rose() {
        super(new ItemStack(Material.DIAMOND_SWORD));
        addSkillFunc("記憶解放-藍薔薇",this::releaseCollection);
    }

    boolean releaseCollection(Player player){
        player.sendMessage("還沒完成");
        return true;
    }


}
