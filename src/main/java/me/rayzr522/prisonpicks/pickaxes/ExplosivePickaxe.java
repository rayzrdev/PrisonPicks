package me.rayzr522.prisonpicks.pickaxes;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.rayzr522.prisonpicks.api.AbstractCustomPickaxe;
import me.rayzr522.prisonpicks.api.PickaxeRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

public class ExplosivePickaxe extends AbstractCustomPickaxe {
    public ExplosivePickaxe(PickaxeRegistry registry) {
        super(registry, "explosive");
    }

    private boolean isBlacklisted(Material type) {
        return getConfig().getStringList("block-blacklist").contains(type.name());
    }

    @Override
    public void onBreak(BlockBreakEvent e) {
        WorldGuardPlugin worldGuard = getPickaxeRegistry().getPlugin().getWorldGuard();

        int radius = getConfig().getInt("radius");

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    Block relative = e.getBlock().getRelative(x, y, z);
                    if (isBlacklisted(relative.getType())) {
                        continue;
                    }

                    if (!worldGuard.createProtectionQuery().testBlockBreak(worldGuard.wrapPlayer(e.getPlayer()), relative)) {
                        continue;
                    }

                    relative.breakNaturally(e.getPlayer().getInventory().getItemInMainHand());
                }
            }
        }

        Location loc = e.getBlock().getLocation().add(0.5, 0.5, 0.5);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
        e.getPlayer().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 1.2f);
    }
}
