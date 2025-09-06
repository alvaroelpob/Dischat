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

    public void sendMessage(String content) {
        if(jda != null) {
            TextChannel channel = jda.getTextChannelById(chatChannelID);
            if (channel != null) {
                channel.sendMessage(content)
                        .setAllowedMentions(List.of(Message.MentionType.USER))
                        .queue();
            }
        }
    }

    public void sendEmbedMessage(String descriptionPath, String colorPath, Player player, String deathMessage, String advancementTitle) {
        if(jda == null) return;

        String description = plugin.getConfig().getString(descriptionPath);
        if(description == null || description.isBlank()) {
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
        if(retrievedColor == null || retrievedColor.isBlank()) {
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
        sendEmbedMessage("messages.server-start", "colors.server-start", null, null, null);
    }

    public void sendStopServerMessage() {
        sendEmbedMessage("messages.server-stop", "colors.server-stop", null, null, null);
    }

    public void sendPlayerJoinMessage(Player player) {
        sendEmbedMessage("messages.player-join", "colors.player-join", player, null, null);
    }

    public void sendPlayerQuitMessage(Player player) {
        sendEmbedMessage("messages.player-quit", "colors.player-quit", player, null, null);
    }

    public void sendDeathMessage(Player player, String deathMessage) {
        sendEmbedMessage("messages.player-death", "colors.player-death", player, deathMessage, null);
    }

    public void sendAdvancementDone(Player player, String advancementTitle) {
        sendEmbedMessage("messages.advancement-done", "colors.advancement-done", player, null, advancementTitle);
    }
}
