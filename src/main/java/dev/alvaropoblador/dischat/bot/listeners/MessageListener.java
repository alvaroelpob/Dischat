package dev.alvaropoblador.dischat.bot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    private final JavaPlugin plugin;
    private final String chatChannelID;

    public MessageListener(JavaPlugin plugin, String chatChannelID) {
        this.plugin = plugin;
        this.chatChannelID = chatChannelID;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.getChannel().getId().equals(chatChannelID)) {
            String author = event.getAuthor().getEffectiveName();
            String content = event.getMessage().getContentDisplay();

            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.broadcastMessage("§9[Discord] §r" + author + ": " + content);
            });
        }
    }
}
