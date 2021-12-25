package com.itiscaleb.utils.events;

import com.itiscaleb.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    static Utils utils = Utils.getInstance();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        ConfigurationSection config = utils.getConfig().getConfigurationSection("titles");
        if(config==null) return;
        String title = config.getString(p.getName());
        if(title==null) {
            p.displayName(Component.text(p.getName()));
            return;
        }
        title = ChatColor.translateAlternateColorCodes('&',title);
        p.displayName(Component.text(title+ChatColor.RESET+" "+p.getName()));
    }
}
