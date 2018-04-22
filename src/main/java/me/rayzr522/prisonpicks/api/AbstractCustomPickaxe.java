package me.rayzr522.prisonpicks.api;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public abstract class AbstractCustomPickaxe implements CustomPickaxe {
    private final PickaxeRegistry registry;
    private final String id;

    public AbstractCustomPickaxe(PickaxeRegistry registry, String id) {
        this.registry = registry;
        this.id = id;

        new PickaxeCommandHandler(registry.getPlugin(), this);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("name"));
    }

    @Override
    public String getLore() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("lore"));
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getDisplayName());
        meta.setLore(Collections.singletonList(getLore()));
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ConfigurationSection getConfig() {
        return registry.getPlugin().getConfig().getConfigurationSection("pickaxes." + getID());
    }

    @Override
    public PickaxeRegistry getPickaxeRegistry() {
        return registry;
    }
}
