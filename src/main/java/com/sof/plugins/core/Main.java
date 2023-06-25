package com.sof.plugins.core;

import com.sof.plugins.core.commands.AddRating;
import com.sof.plugins.core.commands.GetRating;
import com.sof.plugins.core.database.Database;
import com.sof.plugins.core.events.PlayerJoin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        FileConfiguration config = getConfig();
        config.addDefault("test_value", "");
        saveConfig();

        try {
            Database.init();
        } catch (SQLException e) {
            System.out.println(e);
        }

        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("addrating").setExecutor(new AddRating());
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getCommand("getrating").setExecutor(new GetRating());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
