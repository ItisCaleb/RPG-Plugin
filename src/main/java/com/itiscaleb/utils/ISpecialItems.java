package com.itiscaleb.utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public interface ISpecialItems {

     void rightClickEvent(Player player, ItemStack stack);
     void shiftRightClickEvent(Player player, ItemStack stack);
     void killEvent(LivingEntity e,LivingEntity killer, ItemStack stack);
     ItemStack getItem();

}
