package me.rayzr522.prisonpicks.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class Settings {
    public ConfigurationSection config;

    public void load(ConfigurationSection config) {
        this.config = config;
    }

    public List<String> getBlacklistedWorlds() {
        return config.getStringList("blacklisted-worlds");
    }
}
