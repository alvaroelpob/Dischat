package dev.alvaropoblador.dischat.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import dev.alvaropoblador.dischat.bot.listeners.MessageListener;
import dev.alvaropoblador.dischat.bot.listeners.InteractionListener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DiscordBot {
    private final JDA jda;
    private final JavaPlugin plugin;
    private final String chatChannelID;

    public DiscordBot(String token, String chatChannelID, JavaPlugin plugin) throws InterruptedException {

        this.plugin = plugin;
        this.chatChannelID = chatChannelID;

        this.jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        new MessageListener(plugin, chatChannelID),
                        new InteractionListener(plugin)
                )
                .build();

        // optionally block until JDA is ready
        jda.awaitReady();

        TextChannel channel = jda.getTextChannelById(chatChannelID);
        if (channel != null) {
            channel.getGuild().updateCommands().addCommands(
                    Commands.slash("playerlist", "Shows the list of online players")
            ).queue();
        }
    }

    public void shutdown() {
        jda.shutdown();
    }

    public void updateChannelTopic() {
        if(jda != null) {
            TextChannel channel = jda.getTextChannelById(chatChannelID);
            if(channel != null) {
                int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
                int maxPlayers = plugin.getServer().getMaxPlayers();

                channel.getManager().setTopic(onlinePlayers + "/" + maxPlayers + " online players").queue();
            }
        }
    }

    public void sendPlayerMessage(Player player, String content) {
        if(jda != null) {
            TextChannel channel = jda.getTextChannelById(chatChannelID);
            if (channel != null) {
                String message = plugin.getConfig().getString("minecraft-to-discord.player-message");
                if(message == null || message.isEmpty()) {
                    plugin.getLogger().warning("Missing or empty message in config.yml at: " + "minecraft-to-discord.player-message");
                    return;
                }

                message = message.replace("%player_name%", ChatColor.stripColor(player.getName()));
                message = message.replace("%player_display_name%", ChatColor.stripColor(player.getDisplayName()));
                message = message.replace("%message%", ChatColor.stripColor(content));

                channel.sendMessage(message)
                        .setAllowedMentions(List.of(Message.MentionType.USER))
                        .queue();
            }
        }
    }

    public void sendEmbedMessage(String descriptionPath, String colorPath, Player player, String deathMessage, String advancementTitle) {
        if(jda == null) return;

        String description = plugin.getConfig().getString(descriptionPath);
        if(description == null || description.isEmpty()) {
            plugin.getLogger().warning("Missing or empty message in config.yml at: " + descriptionPath);
            return;
        }

        if(player != null) {
            description = description.replace("%player_name%", ChatColor.stripColor(player.getName()));
            description = description.replace("%player_display_name%", ChatColor.stripColor(player.getDisplayName()));
        }

        if(advancementTitle != null) {
            description = description.replace("%advancement_title%", advancementTitle);
        }

        if (deathMessage != null && player != null) {
            String playerName = player.getName();
            String killerPart = deathMessage.replaceFirst("\\b" + playerName + "\\b", "").trim();

            description = description.replace("%death_message%", killerPart);
        }

        String retrievedColor = plugin.getConfig().getString(colorPath);
        if(retrievedColor == null || retrievedColor.isEmpty()) {
            plugin.getLogger().warning("Missing or empty color in config.yml at: " + descriptionPath);
            return;
        }

        int color;
        try {
            if (!retrievedColor.matches("(?i)^#[0-9A-F]{6}$")) {
                throw new IllegalArgumentException("Invalid hex format, must be like #FFFFFF");
            }
            color = Integer.parseInt(retrievedColor.substring(1), 16);
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid color in config.yml at: " + colorPath + " (" + retrievedColor + ").");
            return;
        }


        TextChannel channel = jda.getTextChannelById(chatChannelID);
        if (channel == null) return;

        EmbedBuilder embed = new EmbedBuilder()
                .setDescription(description)
                .setColor(color);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void sendStartServerMessage() {
        sendEmbedMessage("minecraft-to-discord.server-start", "minecraft-to-discord.server-start-embed-color", null, null, null);
    }

    public void sendStopServerMessage() {
        sendEmbedMessage("minecraft-to-discord.server-stop", "minecraft-to-discord.server-stop-embed-color", null, null, null);
    }

    public void sendPlayerJoinMessage(Player player) {
        sendEmbedMessage("minecraft-to-discord.player-join", "minecraft-to-discord.player-join-embed-color", player, null, null);
    }

    public void sendPlayerQuitMessage(Player player) {
        sendEmbedMessage("minecraft-to-discord.player-quit", "minecraft-to-discord.player-quit-embed-color", player, null, null);
    }

    public void sendDeathMessage(Player player, String deathMessage) {
        sendEmbedMessage("minecraft-to-discord.player-death", "minecraft-to-discord.player-death-embed-color", player, deathMessage, null);
    }

    public void sendAdvancementDone(Player player, String advancementTitle) {
        sendEmbedMessage("minecraft-to-discord.advancement-done", "minecraft-to-discord.advancement-done-embed-color", player, null, advancementTitle);
    }
}
