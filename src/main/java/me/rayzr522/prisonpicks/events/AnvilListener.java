package me.rayzr522.prisonpicks.events;

import me.rayzr522.prisonpicks.PrisonPicks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

/**
 * @author Rayzr522
 */
public class AnvilListener implements Listener {
    private PrisonPicks plugin;

    public AnvilListener(PrisonPicks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() == null || e.getInventory().getType() != InventoryType.ANVIL) {
            return;
        }

        if (Arrays.stream(e.getInventory().getContents()).anyMatch(plugin.getPickaxeRegistry()::isCustomPickaxe)) {
            e.setCancelled(true);
        }
    }
}
