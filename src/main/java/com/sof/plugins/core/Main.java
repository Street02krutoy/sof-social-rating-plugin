package com.sof.plugins.core;

import com.sof.plugins.core.database.DatabaseManager;
import com.sof.plugins.core.database.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = getConfig();
        config.addDefault("mongodbUri", "");
        saveConfig();
        DatabaseManager manager = new DatabaseManager(config.getString("mongodbUri"), "sof");
        this.getCommand("addrating").setExecutor((sender, command, label, args) -> {
            if(args.length < 3) {
                sender.sendMessage("Error args");
                return false;
            };
            try {
                int rating = Integer.parseInt(args[1]);
                User user = new User(args[0], manager);
                String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                user.addRating(rating, reason, sender.getName());
                sender.sendMessage("Успешно. Новый рейтинг игрока: "+user.getRating());
                return true;
            }catch (NumberFormatException e) {
                sender.sendMessage("Error isnt num");
                return false;
            }
        });

        this.getCommand("getrating").setExecutor(((sender, command, label, args) -> {
            if (args.length < 1 || args.length > 2) {
                sender.sendMessage("Error args");
                return false;
            }
            ;

            User user = new User(args[0], manager);
            sender.sendMessage("Текущий рейтинг игрока: " + user.getRating() + "\n");
            TableGenerator tg = new TableGenerator(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.CENTER,
                    TableGenerator.Alignment.RIGHT);
            tg.addRow("Модератор", "Рейтинг", "Причина");
            for (int i = user.getReasons().size() - 1; i >= 0; i--) {
                Log log = user.getReasons().get(i);
                tg.addRow(log.getPlayer(), String.valueOf(log.getAmount()), log.getReason());
            }
            ChatPaginator.ChatPage chatPage = null;
            try {
                int page = Integer.parseInt(args[1]);
                System.out.println(page);
                chatPage = ChatPaginator.paginate(String.join("\n", tg.generate(TableGenerator.Receiver.CLIENT, false, true)), page, ChatPaginator.UNBOUNDED_PAGE_WIDTH, ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                chatPage = ChatPaginator.paginate(String.join("\n", tg.generate(TableGenerator.Receiver.CLIENT, false, true)), 1, ChatPaginator.UNBOUNDED_PAGE_WIDTH, ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT);
            }


            sender.sendMessage(String.join("\n", chatPage.getLines()), String.format("%d/%d", chatPage.getPageNumber(), chatPage.getTotalPages()));
            return true;
        }));
        this.getCommand("setrating").setTabCompleter((sender, command, label, args) -> new ArrayList<String>(){{
            add("player");
            add("rating");
            add("reason");
        }});
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
