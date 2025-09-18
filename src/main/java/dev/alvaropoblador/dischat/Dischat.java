package dev.alvaropoblador.dischat;

import dev.alvaropoblador.dischat.services.ChatListener;
import dev.alvaropoblador.dischat.bot.DiscordBot;

import dev.alvaropoblador.dischat.services.Commands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Dischat extends JavaPlugin {
    private DiscordBot discordBot;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        String token = getConfig().getString("token");
        String chatChannelID = getConfig().getString("chatChannelID");

        if (
                token == null ||
                token.equalsIgnoreCase("YOUR_BOT_TOKEN") ||
                chatChannelID == null ||
                chatChannelID.equalsIgnoreCase("YOUR_CHAT_CHANNEL_ID")
        ) {
            getLogger().severe("Invalid configuration detected! Please set a valid Discord bot token and channel ID in config.yml.");
            getLogger().severe("Disabling plugin...");

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            this.discordBot = new DiscordBot(token, chatChannelID, this);

            /* Commands */
            Objects.requireNonNull(getCommand("dischat")).setExecutor(new Commands(this));
            Objects.requireNonNull(getCommand("dc")).setExecutor(new Commands(this));

            /* Listeners */
            getServer().getPluginManager().registerEvents(new ChatListener(discordBot), this);

            getLogger().info(
                    "                                      \n" +
                    "    ____  _           __          __ \n" +
                    "   / __ \\(_)_________/ /_  ____ _/ /_\n" +
                    "  / / / / / ___/ ___/ __ \\/ __ `/ __/\n" +
                    " / /_/ / (__  ) /__/ / / / /_/ / /_  \n" +
                    "/_____/_/____/\\___/_/ /_/\\__,_/\\__/  \n" +
                    "                                     ");

            discordBot.sendStartServerMessage();

            Bukkit.getScheduler().runTaskTimer(this, () -> {
                discordBot.updateChannelTopic();
            }, 0L, 20 * 60L); // 20 ticks * 60 seconds

        } catch (InterruptedException e) {
            getLogger().severe("An error occurred while connecting to the Discord API");
        }
    }

    @Override
    public void onDisable() {
        if(this.discordBot != null) {
            discordBot.sendStopServerMessage();
        }

        getLogger().info("Dischat has been disabled");
    }
}
