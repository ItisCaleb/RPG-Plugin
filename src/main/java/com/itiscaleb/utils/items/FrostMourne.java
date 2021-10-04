package com.itiscaleb.utils.items;

import com.destroystokyo.paper.ParticleBuilder;
import com.itiscaleb.utils.ISpecialItems;
import com.itiscaleb.utils.Utils;
import net.kyori.adventure.text.Component;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FrostMourne implements ISpecialItems {

    static List<Component> Lore = new ArrayList<>();

    static {
        Lore.add(Component.text("§c劍身上刻著的詭異符文微微的發著藍光"));
        Lore.add(Component.text("§c劍鞘上的空洞雙眼正凝視著你"));
        Lore.add(Component.text("§c當你握著這把劍時"));
        Lore.add(Component.text("§c你可以感受到蘊含其中的冰霜與死亡魔力"));
        Lore.add(Component.text("§c就如同踏入死者的國度一般"));
        Lore.add(Component.text("§c與此同時，你也感受到一股強大的力量"));
        Lore.add(Component.text("§c正在嘗試與你對話..."));
        Lore.add(Component.text("§e-------------------------"));
    }

    @Override
    public void rightClickEvent(Player p, ItemStack stack) {
        Location loc = p.getLocation();
        Particle particle = new ParticleBuilder(Particle.SNOWBALL)
                .location(loc)
                .count(20)
                .offset(1,0.5,1)
                .particle();
        AreaEffectCloud effect = (AreaEffectCloud) p.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
        effect.setParticle(particle);
        effect.setVelocity(loc.getDirection().multiply(20));
    }

    @Override
    public void shiftRightClickEvent(Player p, ItemStack stack) {
        NamespacedKey cooldown = new NamespacedKey(Utils.getInstance(), "cooldown");
        ItemMeta meta = stack.getItemMeta();
        long lasttime = meta.getPersistentDataContainer().get(cooldown, PersistentDataType.LONG);
        long time = System.currentTimeMillis();
        if (time - lasttime > 1000 * 60 * 10) {
            calIce(p);
            meta.getPersistentDataContainer().set(cooldown, PersistentDataType.LONG, time);
            stack.setItemMeta(meta);
            p.sendMessage("巫妖王的恐怖力量在你面前展現...");
        } else p.sendMessage("冷卻時間還有" + (60 * 10 - (time - lasttime) / 1000) + "秒");
    }


    void calIce(Player p) {
        Location loc = p.getLocation();
        Vector face = p.getLocation().getDirection().setY(0);
        List<Location> locationList = new ArrayList<>();
        World world = p.getWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 200; i++) {
                    Location center = loc.add(face);
                    locationList.add(center.clone());
                    locationList.add(center.clone().add(face.getZ(), 0, face.getX() * -1));
                    locationList.add(center.clone().add(face.getZ(), 0, face.getX() * -2));
                    locationList.add(center.clone().add(face.getZ() * -1, 0, face.getX()));
                    locationList.add(center.clone().add(face.getZ() * -2, 0, face.getX()));
                }
                for (Location temp : locationList) {
                    setIce(world, temp);
                }
            }
        }.runTask(Utils.getInstance());
    }

    void setIce(World world, Location temp) {
        for (int i = 0; i >= -4; i--) {
            Block b = world.getBlockAt(temp.getBlockX(), temp.getBlockY() + i, temp.getBlockZ());
            if (b.getType() == Material.WATER) {
                b.setType(Material.FROSTED_ICE);
                break;
            }
        }
    }

    @Override
    public void killEvent(LivingEntity e, LivingEntity player, ItemStack stack) {
        NamespacedKey souls = new NamespacedKey(Utils.getInstance(), "souls");
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdata = meta.getPersistentDataContainer();
        int count = pdata.get(souls, PersistentDataType.INTEGER) + 1;
        pdata.set(souls, PersistentDataType.INTEGER, count);
        meta.lore(getLore(count));
        Integer atk = calculateATK(count);
        if (atk > calculateATK(count - 1)) {
            player.sendMessage("§c霜之哀傷的飢渴暫時得到了滿足...");
        }
        speak(player);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "attack", atk + 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        stack.setItemMeta(meta);
        e.sendMessage("§c你感覺到你流失了一部分的生命力...");
    }

    Integer calculateATK(Integer souls) {
        if (souls == 0) return 0;
        return (int) (Math.log10(souls) / Math.log10(5));
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdata = meta.getPersistentDataContainer();
        meta.displayName(Component.text("§6§l霜之哀傷"));
        meta.setUnbreakable(true);
        meta.lore(getLore(0));
        meta.setCustomModelData(1);
        NamespacedKey weapon = new NamespacedKey(Utils.getInstance(), "weapon-type");
        NamespacedKey cooldown = new NamespacedKey(Utils.getInstance(), "cooldown");
        NamespacedKey souls = new NamespacedKey(Utils.getInstance(), "souls");
        pdata.set(weapon, PersistentDataType.STRING, this.getClass().getName());
        pdata.set(cooldown, PersistentDataType.LONG, 0L);
        pdata.set(souls, PersistentDataType.INTEGER, 0);
        AttributeModifier attack = new AttributeModifier(UUID.randomUUID(), "attack", 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier speed = new AttributeModifier(UUID.randomUUID(), "speed", -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attack);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speed);
        item.setItemMeta(meta);
        return item;
    }

    List<Component> getLore(int count) {
        List<Component> lore = new ArrayList<>(Lore);
        lore.add(Component.text("§e靈魂數量:§c§l" + count));
        return lore;
    }

    void speak(LivingEntity p) {
        String[] text = {
                "§c巫妖王很看好你的力量...",
                "§c死亡之力聽你號令...",
                "§c服侍你的主人...",
                "§c北裂境的冰冷寒風會摧毀我們的敵人...",
                "§c我看到了未來，一個由我們掌控的世界..."
        };
        if (Math.random() * 50 <= 1) {
            p.sendMessage(text[(int) (Math.random() * 5)]);
        }
    }
}
