package dev.alvaropoblador.dischat.bot.listeners;

import dev.alvaropoblador.dischat.bot.utils.PlayerListUtil;
import dev.alvaropoblador.dischat.enums.DiscordCommandMode;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.bukkit.plugin.java.JavaPlugin;

public class InteractionListener extends ListenerAdapter {
    private final JavaPlugin plugin;
    private final DiscordCommandMode discordCommandMode;

    public InteractionListener(JavaPlugin plugin, DiscordCommandMode discordCommandMode) {
        this.plugin = plugin;
        this.discordCommandMode = discordCommandMode;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("playerlist")) {
            if (
                    discordCommandMode == DiscordCommandMode.SLASH ||
                    discordCommandMode == DiscordCommandMode.BOTH
            ) {

                PlayerListUtil util = new PlayerListUtil(plugin);
                String replyMessage = util.buildPlayerListMessage();

                event.reply(replyMessage).queue();
            }
        }
    }
}
