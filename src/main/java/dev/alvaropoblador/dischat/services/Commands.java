package dev.alvaropoblador.dischat.services;

import dev.alvaropoblador.dischat.Dischat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    private final Dischat plugin;

    public Commands(Dischat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("dischat") && !command.getName().equalsIgnoreCase("dc")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("§eAvailable commands:");
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("dischat.admin")) {
                    sender.sendMessage("§cYou do not have permission to execute this command.");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage("§aDischat configuration reloaded successfully!");
                return true;

            case "help":
                sendHelp(sender);
                return true;

            default:
                sender.sendMessage("§cUnknown command. Type /dischat help for a list of commands.");
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        if (sender.hasPermission("dischat.admin")) {
            sender.sendMessage("§7/dischat reload §8- §fReloads the plugin configuration");
            sender.sendMessage("§7/dischat help §8- §fShows this help message");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("dischat") && !command.getName().equalsIgnoreCase("dc")) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("dischat.admin")) {
                String[] subcommands = {"reload", "help"};
                for (String sub : subcommands) {
                    if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(sub);
                    }
                }
            }
        }

        return completions;
    }
}