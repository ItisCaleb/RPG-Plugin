package com.itiscaleb.utils.interfaces;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public interface ISpecialItem {

     void rightClickEvent(Player player, ItemStack stack);
     void shiftRightClickEvent(Player player, ItemStack stack);
     void killEvent(LivingEntity e,LivingEntity killer, ItemStack stack);
     ItemStack getItem();

     interface Callback{
          void callback(Player player);
     }
}