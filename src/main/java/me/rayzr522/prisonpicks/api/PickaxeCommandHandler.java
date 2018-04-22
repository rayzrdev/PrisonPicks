package me.rayzr522.prisonpicks.api;

import me.rayzr522.prisonpicks.PrisonPicks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class PickaxeCommandHandler implements CommandExecutor {
    private final PrisonPicks plugin;
    private final CustomPickaxe pickaxe;

    public PickaxeCommandHandler(PrisonPicks plugin, CustomPickaxe pickaxe) {
        this.plugin = plugin;
        this.pickaxe = pickaxe;

        getPluginCommand().setExecutor(this);
    }

    private PluginCommand getPluginCommand() {
        return plugin.getCommand(getCommandLabel());
    }

    private String getCommandLabel() {
        return String.format("%spickaxe", pickaxe.getID());
    }

    @SuppressWarnings("deprecated")
    private Player getPlayer(String name) {
        List<Player> players = Bukkit.matchPlayer(name);

        return players.size() < 1 ? null : players.get(0);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.checkPermission(sender, "admin", true)) {
            return true;
        }

        if (!(sender instanceof Player) && args.length < 1) {
            showUsage(sender);
            return true;
        }

        Player target = args.length > 0 ? getPlayer(args[0]) : (Player) sender;
        if (target == null) {
            // Only will happen if the player was fetched via args
            sender.sendMessage(plugin.tr("command.fail.no-player", args[0]));
            return true;
        }

        target.getInventory().addItem(pickaxe.createItem());
        sender.sendMessage(plugin.tr("command.pickaxe.given", target.getName(), pickaxe.getDisplayName()));

        return true;
    }

    private void showUsage(CommandSender sender) {
        sender.sendMessage(plugin.tr("command.pickaxe.usage", getCommandLabel()));
    }
}
