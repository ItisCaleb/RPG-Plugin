package com.itiscaleb.utils.items;

import com.destroystokyo.paper.ParticleBuilder;
import com.itiscaleb.utils.Utils;

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

public class FrostMourne extends SpecialWeapon {

    public FrostMourne(ItemStack item) {
        super(item);
        addSkillFunc("北裂境之怒", this::buildBridge);
        addSkillFunc("凜風衝擊", this::frostWave);
    }




    void frostWave(Player p){
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


    void buildBridge(Player p) {
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
