package com.itiscaleb.utils.items;


import com.itiscaleb.utils.Configs;
import com.itiscaleb.utils.Utils;
import com.itiscaleb.utils.interfaces.ISpecialItem;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public abstract class SpecialWeapon implements ISpecialItem {
    private final ConfigurationSection config;
    protected ItemStack item;
    protected String displayName;
    protected String lore;
    protected HashMap<String, Skill> skills = new HashMap<>();
    private final HashMap<String, Callback> skillFunc = new HashMap<>();

    static HashMap<String, SpecialWeapon> WEAPON_MAP = new HashMap<>();

    private static class Skill{
        int id;
        String name;
        double cd;
        Skill(int id,String name,double cd){
            this.id = id;
            this.name = name;
            this.cd = cd;
        }
    }

    //insensitive
    public static void registerWeapon(SpecialWeapon weapon) {
        String name = weapon.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        WEAPON_MAP.put(name, weapon);
        Utils.getInstance().getLogger().info("Successfully load weapon " + name);
    }

    public static SpecialWeapon getWeapon(String name) {
        name = name.toLowerCase(Locale.ROOT);
        return WEAPON_MAP.getOrDefault(name, null);
    }

    static long secondToMilis(double second) {
        return (long) (second * 1000);
    }

    public SpecialWeapon(ItemStack item) {
        String name = this.getClass().getSimpleName();
        this.config = Configs.getWeaponAttr().getConfigurationSection(name);
        this.item = item;
        if (this.config == null) {
            Utils.Log("你還沒定義這把武器的屬性");
            return;
        }
        this.displayName = config.getString("displayName");
        this.lore = config.getString("lore");
        setBaseMeta();
    }

    //add skill name and cd to map
    private void setSkills(ItemMeta meta) {
        List<Map<?,?>> skill = config.getMapList("skills");
        int i = 0;
        //use index to track individual cool down
        for (Map<?,?> map: skill) {
            String name = (String) map.get("name");
            this.skills.put(name, new Skill(i,name, (double) map.get("cd")));
            setPersistentData(meta,i+"-cd","Long",0L);
            i++;
        }
    }

    protected Skill getCurrentSkill(ItemMeta meta) {
        return skills.get((String) getPersistentData(meta, "skill", "String"));
    }

    protected void addSkillFunc(String name, Callback cb) {
        skillFunc.put(name, cb);
    }

    private void setBaseMeta() {
        String name = this.getClass().getSimpleName();
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§6§l" + displayName));
        meta.setCustomModelData(1);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        setSkills(meta);
        setPersistentData(meta, "weapon-type", "String", name);
        setPersistentData(meta, "skill", "String", skills.keySet().toArray()[0]);
        setPersistentData(meta, "lastChange", "Long", 0L);
        setDefaultAttribute(meta);
        setLore(meta);
        item.setItemMeta(meta);
    }


    private void setDefaultAttribute(ItemMeta meta) {
        ConfigurationSection attr = config.getConfigurationSection("attr");
        if (attr == null) return;
        for (String name : attr.getKeys(false)) {
            double value = attr.getDouble(name);
            switch (name) {
                case "atk" -> setAttributeAdd(meta,Attribute.GENERIC_ATTACK_DAMAGE, "attack", value);
                case "atk_speed" -> setAttributeAdd(meta,Attribute.GENERIC_ATTACK_SPEED, "attack_speed", Math.max(value - 4, -4));
                case "health" -> setAttributeAdd(meta,Attribute.GENERIC_MAX_HEALTH, "health", value);
                case "speed" -> setAttributeAdd(meta,Attribute.GENERIC_MOVEMENT_SPEED, "speed", value);
            }
        }
    }


    //TODO: finish custom resource
    /*private String setResource(){
        String lore = config.getString("lore");
        ConfigurationSection resources = config.getConfigurationSection("resources");
        if(resources == null) return lore;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdata = meta.getPersistentDataContainer();
        ArrayList<Object> value = new ArrayList<>();
        for (String name :resources.getKeys(false)) {
            setPersistentData(meta,name,,resources.getString(name));
            switch (resources.getString(name)) {
                case "integer", "double", "long" -> value.add(0);
                case "string" -> value.add("");
            }
        }
        return String.format(lore,value);
    }

    public void setCustomData(PersistentDataContainer pdata,String name,String type,Object value){
        NamespacedKey key = new NamespacedKey(Utils.getInstance(), name);
        switch (type.toLowerCase(Locale.ROOT)){
            case "integer"->
                    pdata.set(key, PersistentDataType.INTEGER,(int)value);
            case "double"->
                    pdata.set(key,PersistentDataType.DOUBLE,(double)value);
            case "long"->
                    pdata.set(key,PersistentDataType.LONG,(long)value);
            case "string"->
                    pdata.set(key,PersistentDataType.STRING,(String) value);
        }
    }

    private void setCustomData(PersistentDataContainer pdata,String name,String type){
        NamespacedKey key = new NamespacedKey(Utils.getInstance(), name);
        switch (type.toLowerCase(Locale.ROOT)){
            case "integer"->
                    pdata.set(key, PersistentDataType.INTEGER,0);
            case "double"->
                    pdata.set(key,PersistentDataType.DOUBLE,0d);
            case "long"->
                    pdata.set(key,PersistentDataType.LONG,0L);
            case "string"->
                    pdata.set(key,PersistentDataType.STRING,"");
        }
    }*/

    private void setLore(ItemMeta meta) {
        //add lore
        String[] lines = lore.split("\n");
        List<Component> components = new ArrayList<>();
        for (String str : lines) {
            components.add(Component.text(str));
        }

        //add skill
        components.add(Component.text("§4-----------技能------------"));
        Skill currentSkill = getCurrentSkill(meta);
        for (String skill : skills.keySet()) {
            if (Objects.equals(skill, currentSkill.name)) {
                components.add(Component.text("§a-> " + skill + " " + skills.get(skill).cd+"秒"));
            } else components.add(Component.text("- " + skill + " " + skills.get(skill).cd+"秒"));
        }

        //add attribute
        components.add(Component.text(""));
        components.add(Component.text("§7在慣用手時:"));
        Set<Attribute> key = meta.hasAttributeModifiers()
                ? meta.getAttributeModifiers().keySet()
                : Collections.emptySet();
        for (Attribute attr : key) {
            for (AttributeModifier modifier: meta.getAttributeModifiers().get(attr)){
                double amount = modifier.getAmount();
                switch (modifier.getName()) {
                    case "attack" -> components.add(Component.text(" §2" + amount + " 攻擊傷害"));
                    case "attack_speed" -> components.add(Component.text(" §2" + (4+amount) + " 攻擊速度"));
                    case "health" -> components.add(Component.text(" §2+" + amount + " 點最大生命值"));
                    case "speed" -> components.add(Component.text(" §2+" + amount + " 點速度"));
                }
            }

        }
        meta.lore(components);
    }

    private void setAttributeAdd(ItemMeta meta,Attribute attr, String name, double value) {
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), name, value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(attr, modifier);
    }

    //TODO: attribute scalar and multiply

    protected Object getPersistentData(ItemMeta meta, String name, String type) {
        NamespacedKey key = new NamespacedKey(Utils.getInstance(), name);
        return switch (type.toLowerCase(Locale.ROOT)) {
            case "string" -> meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            case "integer" -> meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            case "long" -> meta.getPersistentDataContainer().get(key, PersistentDataType.LONG);
            case "double" -> meta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
            default -> null;
        };
    }

    protected void setPersistentData(ItemMeta meta, String name, String type, Object value) {
        NamespacedKey key = new NamespacedKey(Utils.getInstance(), name);
        switch (type.toLowerCase(Locale.ROOT)) {
            case "string" -> meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, (String) value);
            case "integer" -> meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, (Integer) value);
            case "long" -> meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, (Long) value);
            case "double" -> meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, (Double) value);
        }
    }

    @Override
    public void rightClickEvent(Player player, ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        Skill skill = getCurrentSkill(meta);
        long time = System.currentTimeMillis();
        long lasttime = (long) getPersistentData(meta, skill.id+"-cd", "Long");
        //execute skill
        if (time - lasttime >= secondToMilis(skill.cd)) {
            skillFunc.get(skill.name).callback(player);
            //set new skill timer
            setPersistentData(meta, skill.id+"-cd", "Long", time);
            stack.setItemMeta(meta);
            player.sendMessage("§a使用技能 §e§l" + skill.name);
        } else player.sendMessage(String.format("冷卻時間還有%.1f秒",(skill.cd - (float)(time - lasttime)/1000)));
    }

    //for change skill
    @Override
    public void shiftRightClickEvent(Player player, ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        long time = System.currentTimeMillis();
        long lasttime = (long) getPersistentData(meta, "lastChange", "Long");
        if (time - lasttime >= secondToMilis(0.5)) {
            ArrayList<String> names = new ArrayList<>(skills.keySet());
            String currentSkill = (String) getPersistentData(meta, "skill", "String");
            //find next skill
            String nextSkill = names.get((names.indexOf(currentSkill) + 1) % names.size());
            setPersistentData(meta, "skill", "String", nextSkill);
            //set new change skill timer
            setPersistentData(meta, "lastChange", "Long", time);
            setLore(meta);
            //remember to set meta
            stack.setItemMeta(meta);
            player.sendMessage("§a切換技能至 §e§l" + nextSkill);
        } else player.sendMessage(String.format("冷卻時間還有%.1f秒",(0.5 - (float)(time - lasttime)/1000)));
    }

    @Override
    abstract public void killEvent(LivingEntity e, LivingEntity killer, ItemStack stack);

    @Override
    public ItemStack getItem() {
        return item;
    }
}