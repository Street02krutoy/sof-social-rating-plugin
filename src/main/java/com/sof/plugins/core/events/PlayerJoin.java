package com.sof.plugins.core.events;

import com.sof.plugins.core.Log;
import com.sof.plugins.core.database.DatabaseManager;
import com.sof.plugins.core.database.User;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class PlayerJoin implements Listener {
    private final DatabaseManager manager;

    public PlayerJoin(DatabaseManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = new User(event.getPlayer().getName(), manager);
        //System.out.println(user.getReasons().isEmpty());
        if(user.getReasons().isEmpty()) return;
        Log last = user.getReasons().get(user.getReasons().size()-1);
        event.getPlayer().sendMessage(
                String.format(
                        "Здравствуйте, %s, ваш рейтинг: %d\nПоследнее изменение рейтинга: %d\nПричина: %s\nМодератор: %s",
                        event.getPlayer().getName(),
                        user.getRating(),
                        last.getAmount(),
                        last.getReason(),
                        last.getPlayer()
                )
        );
    }
}
