package com.sof.plugins.core.events;

import com.sof.plugins.core.Log;
import com.sof.plugins.core.database.Database;
import com.sof.plugins.core.database.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = null;
        try {
            user = Database.getUser(event.getPlayer().getName());
        } catch (SQLException e) {
            System.out.println("Player join err "+event.getPlayer().getName()+"\n "+e.getMessage());
            return;
        }
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
