package me.rayzr522.prisonpicks;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.rayzr522.prisonpicks.api.PickaxeRegistry;
import me.rayzr522.prisonpicks.events.AnvilListener;
import me.rayzr522.prisonpicks.pickaxes.BountifulPickaxe;
import me.rayzr522.prisonpicks.pickaxes.ExplosivePickaxe;
import me.rayzr522.prisonpicks.pickaxes.SmeltersPickaxe;
import me.rayzr522.prisonpicks.util.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Rayzr
 */
public class PrisonPicks extends JavaPlugin {
    private static PrisonPicks instance;
    private Language language = new Language();
    private PickaxeRegistry pickaxeRegistry;
    private WorldGuardPlugin worldGuard;

    public static PrisonPicks getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        if (!loadWorldGuard()) {
            getLogger().severe("Failed to load WorldGuard! This plugin will now be disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        reload();

        pickaxeRegistry = new PickaxeRegistry(this);
        registerDefaultPickaxes();

        getCommand("prisonpicks").setExecutor(new CommandHandler(this));
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
    }

    private boolean loadWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) {
            return false;
        }

        worldGuard = (WorldGuardPlugin) plugin;
        return true;
    }

    private void registerDefaultPickaxes() {
        pickaxeRegistry.registerPickaxe(new SmeltersPickaxe(pickaxeRegistry));
        pickaxeRegistry.registerPickaxe(new ExplosivePickaxe(pickaxeRegistry));
        pickaxeRegistry.registerPickaxe(new BountifulPickaxe(pickaxeRegistry));
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * (Re)loads all configs from the disk
     */
    public void reload() {
        saveDefaultConfig();
        reloadConfig();

        language.load(getConfig().getConfigurationSection("messages"));
    }

    /**
     * Returns a message from the language file
     *
     * @param key     The key of the message to translate
     * @param objects The formatting objects to use
     * @return The formatted message
     */
    public String tr(String key, Object... objects) {
        return language.tr(key, objects);
    }

    /**
     * Returns a message from the language file without adding the prefix
     *
     * @param key     The key of the message to translate
     * @param objects The formatting objects to use
     * @return The formatted message
     */
    public String trRaw(String key, Object... objects) {
        return language.trRaw(key, objects);
    }

    /**
     * Checks a target {@link CommandSender} for a given permission (excluding the permission base). Example:
     * <p>
     * <pre>
     *     checkPermission(sender, "command.use", true);
     * </pre>
     * <p>
     * This would check if the player had the permission <code>"{plugin name}.command.use"</code>, and if they didn't, it would send them the no-permission message from the messages config file.
     *
     * @param target      The target {@link CommandSender} to check
     * @param permission  The permission to check, excluding the permission base (which is the plugin name)
     * @param sendMessage Whether or not to send a no-permission message to the target
     * @return Whether or not the target has the given permission
     */
    public boolean checkPermission(CommandSender target, String permission, boolean sendMessage) {
        String fullPermission = String.format("%s.%s", getName(), permission);

        if (!target.hasPermission(fullPermission)) {
            if (sendMessage) {
                target.sendMessage(tr("no-permission", fullPermission));
            }

            return false;
        }

        return true;
    }

    /**
     * @return The {@link Language} instance for this plugin
     */
    public Language getLang() {
        return language;
    }

    public WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }

    public PickaxeRegistry getPickaxeRegistry() {
        return pickaxeRegistry;
    }
}
