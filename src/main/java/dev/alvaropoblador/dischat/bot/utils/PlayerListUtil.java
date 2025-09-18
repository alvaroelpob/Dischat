package dev.alvaropoblador.dischat.bot.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.stream.Collectors;

public class PlayerListUtil {
    private final Plugin plugin;

    public PlayerListUtil(Plugin plugin) {
        this.plugin = plugin;
    }

    public String buildPlayerListMessage() {
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

        return reply.toString();
    }
}
