package me.rayzr522.prisonpicks.api;

import me.rayzr522.prisonpicks.PrisonPicks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PickaxeRegistry implements Listener {
    private final PrisonPicks plugin;
    private final Map<String, CustomPickaxe> registry = new HashMap<>();

    public PickaxeRegistry(PrisonPicks plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer() == null || e.getBlock() == null) {
            // You can never be too safe with Bukkit :p
            return;
        }

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() != Material.DIAMOND_PICKAXE || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        String name = item.getItemMeta().getDisplayName();
        registry.values().stream()
                .filter(pickaxe -> pickaxe.getDisplayName().equals(name))
                .findFirst()
                .ifPresent(pickaxe -> pickaxe.onBreak(e));
    }

    public void registerPickaxe(CustomPickaxe pickaxe) {
        registry.put(pickaxe.getID(), pickaxe);
    }

    public boolean isCustomPickaxe(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return false;
        }

        return registry.values().stream()
                .map(CustomPickaxe::getDisplayName)
                .anyMatch(item.getItemMeta().getDisplayName()::equals);
    }

    public PrisonPicks getPlugin() {
        return plugin;
    }
}
