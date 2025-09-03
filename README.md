# Dischat

A Minecraft (Spigot/Paper) plugin that bridges chat between your Minecraft server and a Discord channel using [JDA](https://github.com/discord-jda/JDA).

## ✨ Features

- Sync in-game chat messages to a Discord text channel
- Sync Discord messages back into Minecraft chat
- Customizable embed messages for:
    - Server start / stop
    - Player join / quit
- Hex color support for embeds (`#RRGGBB`)
- Simple and lightweight setup

## 📥 Installation

1. Download the [latest release](https://github.com/alvaroelpob/Dischat/releases/tag/latest) of **Dischat**.
2. Place the JAR file into your server's `plugins/` folder.
3. Start the server once to generate the default `config.yml`.
4. Stop the server and configure your `config.yml`.

## 🚀 How does it work?

- Chat in Minecraft → message is sent to Discord
- Chat in Discord → message is sent to Minecraft
- Server start/stop and player join/quit events are sent to Discord

## 🛠️ Available commands

- `/dischat help` - Shows Dischat commands
- `/dischat reload` - Reloads Dischat's config.yml

## ⚙️ Configuration

Example `config.yml`:

```yaml
token: "YOUR_BOT_TOKEN"
chatChannelID: "YOUR_CHAT_CHANNEL_ID"

messages:
  server-start: "**⚡ Server started**"
  server-stop: "**🛑 Server stopped**"
  player-join: "**🟢 %player_display_name%**"
  player-quit: "**🔴 %player_display_name%**"

colors:
  server-start: "#00FF00"
  server-stop: "#FF0000"
  player-join: "#00FF00"
  player-quit: "#FF0000"
```

### Placeholders
- `%player_name%` → The player’s username
- `%player_display_name%` → The player’s display name

**Important:** Replace `YOUR_BOT_TOKEN` and `YOUR_CHAT_CHANNEL_ID` with valid values (requires a server restart).

## 🛠️ Requirements

- Java 17+
- Spigot / Paper 1.17+ (only tested in 1.20 and 1.21)
- A Discord bot token (https://discord.com/developers/applications)