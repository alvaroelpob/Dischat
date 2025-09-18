package dev.alvaropoblador.dischat.bot.listeners;

import dev.alvaropoblador.dischat.bot.utils.PlayerListUtil;
import dev.alvaropoblador.dischat.enums.DiscordCommandMode;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    private final JavaPlugin plugin;
    private final String chatChannelID;
    private final DiscordCommandMode discordCommandMode;

    public MessageListener(JavaPlugin plugin, String chatChannelID, DiscordCommandMode discordCommandMode) {
        this.plugin = plugin;
        this.chatChannelID = chatChannelID;
        this.discordCommandMode = discordCommandMode;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.getChannel().getId().equals(chatChannelID)) {

            if(event.getMessage().getContentDisplay().equals("playerlist")) {
                if (
                        discordCommandMode == DiscordCommandMode.MESSAGE ||
                        discordCommandMode == DiscordCommandMode.BOTH
                ) {
                    PlayerListUtil util = new PlayerListUtil(plugin);
                    String replyMessage = util.buildPlayerListMessage();

                    event.getMessage().reply(replyMessage).queue();
                    return;
                }
            }

            User author = event.getAuthor();
            String content = event.getMessage().getContentDisplay();

            String template = plugin.getConfig().getString("discord-to-minecraft.user-message");
            if(template == null || template.isEmpty()) {
                plugin.getLogger().warning("Missing or empty message in config.yml at: " + "discord-to-minecraft.user-message");
                return;
            }

            String message = template
                    .replace("%author_id%", author.getId())
                    .replace("%author_username%", author.getName())
                    .replace("%author_displayName%", author.getEffectiveName())
                    .replace("%message%", content);

            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            });
        }
    }
}
