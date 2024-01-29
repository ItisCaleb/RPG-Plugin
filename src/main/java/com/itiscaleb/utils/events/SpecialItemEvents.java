package com.itiscaleb.utils.events;

import com.itiscaleb.utils.Utils;
import com.itiscaleb.utils.items.weapons.SpecialWeapon;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpecialItemEvents implements Listener {

    static Utils utils = Utils.getInstance();
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e){
        boolean result = ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getClickedBlock().getType().isInteractable())
                || e.getAction().equals(Action.RIGHT_CLICK_AIR))
                && !e.getPlayer().isSneaking() && e.getHand() == EquipmentSlot.HAND;;
        if(result && e.getItem() != null){
            ItemMeta meta = e.getItem().getItemMeta();
            NamespacedKey space = new NamespacedKey(utils,"weapon-type");
            if(meta.getPersistentDataContainer().has(space, PersistentDataType.STRING)){
                String type = meta.getPersistentDataContainer().get(space,PersistentDataType.STRING);
                SpecialWeapon weapon = SpecialWeapon.getWeapon(type);
                if(weapon == null) return;
                weapon.rightClickEvent(e.getPlayer(), e.getItem());
            }
        }
    }

    @EventHandler
    public void onPlayerShiftRightClick(PlayerInteractEvent e){
        boolean result = ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getClickedBlock().getType().isInteractable())
                || e.getAction().equals(Action.RIGHT_CLICK_AIR))
                && e.getPlayer().isSneaking() && e.getHand() == EquipmentSlot.HAND;
        if(result && e.getItem() != null){
            ItemMeta meta = e.getItem().getItemMeta();
            NamespacedKey space = new NamespacedKey(utils,"weapon-type");
            if(meta.getPersistentDataContainer().has(space, PersistentDataType.STRING)){
                String type = meta.getPersistentDataContainer().get(space,PersistentDataType.STRING);
                SpecialWeapon weapon = SpecialWeapon.getWeapon(type);
                if(weapon == null) return;
                weapon.shiftRightClickEvent(e.getPlayer(), e.getItem());
            }
        }
    }

    @EventHandler
    public void onPlayerKill(EntityDamageByEntityEvent e){
        /*Entity entity = e.getEntity();
        boolean result = e.getDamager() instanceof Player && (entity instanceof Mob || entity instanceof HumanEntity);
        if(result) {
            Player player = (Player) e.getDamager();
            LivingEntity damaged = (LivingEntity) e.getEntity();
            if(e.getFinalDamage() >= damaged.getHealth()){
                ItemStack stack = player.getInventory().getItemInMainHand();
                ItemMeta meta = stack.getItemMeta();
                NamespacedKey space = new NamespacedKey(utils,"weapon-type");
                if(meta.getPersistentDataContainer().has(space, PersistentDataType.STRING)){
                    String type = meta.getPersistentDataContainer().get(space,PersistentDataType.STRING);
                    try {
                        Class<?> c = Class.forName(type);
                        Object obj = c.getDeclaredConstructor().newInstance();
                        ISpecialItem items = (ISpecialItem)obj;
                        items.killEvent(damaged,player, stack);
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }
                }
            }
        }*/
    }

    //TODO: keep showing current skill
    @EventHandler
    public void  onPlayerChangeItem(PlayerItemHeldEvent e){
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            String skill = (String) SpecialWeapon.getPersistentData(meta,"skill","String");
            if(skill != null){
                e.getPlayer().sendActionBar(Component.text("§a目前技能 §e§l"+skill));
            }
        }
    }



}
