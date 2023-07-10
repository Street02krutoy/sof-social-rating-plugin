package com.sof.plugins.core.commands;

import com.sof.plugins.core.database.Database;
import com.sof.plugins.core.database.User;
import com.sof.plugins.core.errors.UserNotExistException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Arrays;

public class AddRating implements CommandExecutor {


    public AddRating() {

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {if(args.length < 3) {
        sender.sendMessage("Error args");
        return false;
    };
        try {
            int rating = Integer.parseInt(args[1]);
            User user = Database.getUser(args[0]);
            String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            Database.addRating(user, rating, reason, sender.getName());
            sender.sendMessage("Успешно. Новый рейтинг игрока: "+user.getRating());
            return true;
        }catch (NumberFormatException e) {
            sender.sendMessage("Error isnt num");
            return false;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }catch (UserNotExistException e) {
            sender.sendMessage("Такого игрока не существует.");
            return true;
        }
    }
}
