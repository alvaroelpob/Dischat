package dev.alvaropoblador.dischat.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class UpdateChecker {
    private final JavaPlugin plugin;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String getVersion() {
        try {
            URI uri = new URI("https://alvaropoblador.dev/api/projects/dischat");
            HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            reader.close();
            con.disconnect();

            return json.get("version").getAsString();
        } catch (Exception e) {
            plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            return null;
        }
    }
}