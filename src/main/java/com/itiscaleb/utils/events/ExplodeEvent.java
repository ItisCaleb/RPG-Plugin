package com.itiscaleb.utils.events;


import com.itiscaleb.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeEvent implements Listener {
    static Utils utils = Utils.getInstance();

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e){
        if(!utils.getConfig().getBoolean("rules.TNT_explode")){
            e.blockList().removeIf(block -> !block.getType().equals(Material.TNT));
        }
    }
}
