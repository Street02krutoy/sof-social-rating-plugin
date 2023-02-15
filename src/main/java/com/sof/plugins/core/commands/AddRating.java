package com.sof.plugins.core.commands;

import com.sof.plugins.core.database.DatabaseManager;
import com.sof.plugins.core.database.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AddRating implements CommandExecutor {

    private final DatabaseManager manager;

    public AddRating(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {if(args.length < 3) {
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
    }
}
