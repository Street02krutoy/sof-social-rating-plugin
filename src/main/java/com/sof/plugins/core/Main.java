package com.sof.plugins.core;

import com.sof.plugins.core.commands.AddRating;
import com.sof.plugins.core.commands.GetRating;
import com.sof.plugins.core.database.DatabaseManager;
import com.sof.plugins.core.database.User;
import com.sof.plugins.core.events.PlayerJoin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = getConfig();
        config.addDefault("mongodbUri", "");
        saveConfig();
        DatabaseManager manager = new DatabaseManager(config.getString("mongodbUri"), "sof");
        this.registerCommands(manager);
    }

    private void registerCommands(DatabaseManager manager) {
        this.getCommand("addrating").setExecutor(new AddRating(manager));
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(manager), this);
        this.getCommand("getrating").setExecutor(new GetRating(manager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
