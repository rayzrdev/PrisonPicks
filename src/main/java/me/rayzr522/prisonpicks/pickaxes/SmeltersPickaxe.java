package me.rayzr522.prisonpicks.pickaxes;

import me.rayzr522.prisonpicks.api.AbstractCustomPickaxe;
import me.rayzr522.prisonpicks.api.PickaxeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SmeltersPickaxe extends AbstractCustomPickaxe {
    private List<FurnaceRecipe> furnaceRecipes = new ArrayList<>();

    public SmeltersPickaxe(PickaxeRegistry registry) {
        super(registry, "smelters");

        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof FurnaceRecipe) {
                furnaceRecipes.add((FurnaceRecipe) recipe);
            }
        });
    }

    private boolean isSmeltable(Material type) {
        return getConfig().getStringList("smeltable-blocks").contains(type.name());
    }

    private Optional<ItemStack> getResult(ItemStack input) {
        ItemStack item = input.clone();
        if (item.getDurability() == 0) {
            // Hack for Bukkit's stupidity
            item.setDurability(Short.MAX_VALUE);
        }

        return furnaceRecipes.stream()
                .filter(recipe -> recipe.getInput().equals(item))
                .findFirst()
                .map(FurnaceRecipe::getResult);
    }

    private ItemStack smeltIfPossible(ItemStack item) {
        return getResult(item).orElse(item);
    }

    @Override
    public void onBreak(BlockBreakEvent e) {
        if (!isSmeltable(e.getBlock().getType())) {
            return;
        }

        Collection<ItemStack> drops = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());

        e.getBlock().setType(Material.AIR);
        e.setDropItems(false);

        Location location = e.getBlock().getLocation().add(0.5, 0, 0.5);
        drops.stream()
                .map(this::smeltIfPossible)
                .forEach(itemStack -> location.getWorld().dropItem(location, itemStack));
    }
}
