package me.rayzr522.prisonpicks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
    private final PrisonPicks plugin;

    public CommandHandler(PrisonPicks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.checkPermission(sender, "admin", true)) {
            return true;
        }


        String sub = args.length < 1 ? "" : args[0].toLowerCase();
        if (sub.equals("reload")) {
            plugin.reload();
            sender.sendMessage(plugin.tr("command.prisonpicks.reloaded"));
        } else if (sub.equals("version")) {
            sender.sendMessage(plugin.tr("command.prisonpicks.version", plugin.getDescription().getVersion()));
        } else {
            sender.sendMessage(plugin.tr("command.prisonpicks.usage"));
        }

        return true;
    }
}
