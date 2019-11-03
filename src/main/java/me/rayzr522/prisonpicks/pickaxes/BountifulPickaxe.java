package me.rayzr522.prisonpicks.pickaxes;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.rayzr522.prisonpicks.api.AbstractCustomPickaxe;
import me.rayzr522.prisonpicks.api.PickaxeRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public class BountifulPickaxe extends AbstractCustomPickaxe {
    public BountifulPickaxe(PickaxeRegistry registry) {
        super(registry, "bountiful");
    }

    private int getWorth(Material type) {
        List<String> materials = getConfig().getStringList("ore-worth");
        for (int i = 0; i < materials.size(); i++) {
            if (type.name().equals(materials.get(i))) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onBreak(BlockBreakEvent e) {
        int highestWorth = -1;
        Block mostValuableBlock = null;
        WorldGuardPlugin worldGuard = getPickaxeRegistry().getPlugin().getWorldGuard();

        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    Block relative = e.getBlock().getRelative(x, y, z);
                    if (relative == null) {
                        continue;
                    }

                    if (!worldGuard.createProtectionQuery().testBlockBreak(worldGuard.wrapPlayer(e.getPlayer()), relative)) {
                        // Ignore blocks outside our access.
                        continue;
                    }

                    int worth = getWorth(relative.getType());
                    if (worth > highestWorth) {
                        highestWorth = worth;
                        mostValuableBlock = relative;
                    }
                }
            }
        }

        if (highestWorth > -1) {
            Collection<ItemStack> drops = mostValuableBlock.getDrops(e.getPlayer().getInventory().getItemInMainHand());

            e.getBlock().setType(Material.AIR);
            e.setDropItems(false);

            Location loc = e.getBlock().getLocation().add(0.5, 0, 0.5);
            drops.forEach(itemStack -> loc.getWorld().dropItem(loc, itemStack));
        }
    }
}
