package me.rayzr522.prisonpicks.api;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public interface CustomPickaxe {
    String getID();

    String getDisplayName();

    String getLore();

    ItemStack createItem();

    ConfigurationSection getConfig();

    PickaxeRegistry getPickaxeRegistry();

    void onBreak(BlockBreakEvent e);
}
