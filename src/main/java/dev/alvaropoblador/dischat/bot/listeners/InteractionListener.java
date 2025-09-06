package dev.alvaropoblador.dischat.bot.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.stream.Collectors;

public class InteractionListener extends ListenerAdapter {
    private final JavaPlugin plugin;

    public InteractionListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("playerlist")) {
            Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
            int maxPlayers = plugin.getServer().getMaxPlayers();
            int onlineCount = onlinePlayers.size();

            StringBuilder reply = new StringBuilder("**Online Players: " + onlineCount + "/" + maxPlayers + "**");

            if (!onlinePlayers.isEmpty()) {
                String playerList = onlinePlayers.stream()
                        .map(Player::getDisplayName)
                        .collect(Collectors.joining(", "));

                reply.append("\n```").append(playerList).append("```");
            }

            event.reply(reply.toString()).setEphemeral(true).queue();
        }
    }
}
