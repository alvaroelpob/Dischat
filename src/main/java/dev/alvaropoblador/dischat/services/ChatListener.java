package dev.alvaropoblador.dischat.services;

import dev.alvaropoblador.dischat.bot.DiscordBot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatListener implements Listener {
    private final DiscordBot discordBot;

    public ChatListener(DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    @EventHandler
    private void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = ChatColor.stripColor(event.getPlayer().getDisplayName());
        String message = event.getMessage();

        discordBot.sendMessage("**" + playerName + "** > " + message);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        discordBot.sendPlayerJoinMessage(event.getPlayer());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        discordBot.sendPlayerQuitMessage(event.getPlayer());
    }
}
