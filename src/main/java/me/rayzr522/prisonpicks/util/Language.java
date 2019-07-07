package me.rayzr522.prisonpicks.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Language {
    private Map<String, String> messages;

    public void load(ConfigurationSection config) {
        messages = config.getKeys(true)
                .stream()
                .collect(Collectors.toMap(key -> key, key -> Objects.toString(config.get(key))));
    }

    private String computePrefixFor(String key) {
        String base = key.lastIndexOf('.') > -1 ? key.substring(0, key.lastIndexOf('.')) : "";

        String prefix = messages.getOrDefault(base + ".prefix", messages.getOrDefault("prefix", ""));
        String addon = messages.getOrDefault(base + ".prefix-addon", "");

        return prefix + addon;
    }

    public String trRaw(String key, Object... formatterArgs) {
        return String.format(messages.getOrDefault(key, key), formatterArgs);
    }

    public String tr(String key, Object... formatterArgs) {
        return ChatColor.translateAlternateColorCodes('&', computePrefixFor(key) + trRaw(key, formatterArgs));
    }
}
