package com.sof.plugins.core.commands;

import com.sof.plugins.core.Log;
import com.sof.plugins.core.TableGenerator;
import com.sof.plugins.core.database.DatabaseManager;
import com.sof.plugins.core.database.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;

public class GetRating implements CommandExecutor {

    private final DatabaseManager manager;

    public GetRating(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || args.length > 2) {
            sender.sendMessage("Error args");
            return false;
        }

        User user = new User(args[0], manager);
        sender.sendMessage("Текущий рейтинг игрока: " + user.getRating() + "\n");
        if(user.getReasons().isEmpty()) {
            sender.sendMessage("У игрока нет изменений рейтинга на данный момент");
            return true;
        }
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
            chatPage = ChatPaginator.paginate(String.join("\n", tg.generate(TableGenerator.Receiver.CLIENT, false)), page, ChatPaginator.UNBOUNDED_PAGE_WIDTH, ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
            chatPage = ChatPaginator.paginate(String.join("\n", tg.generate(TableGenerator.Receiver.CLIENT, false)), 1, ChatPaginator.UNBOUNDED_PAGE_WIDTH, ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT);
        }

        sender.sendMessage(chatPage.getLines());
        TextComponent back = new TextComponent("<-");
        back.setClickEvent(
                new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/getrating "+ args[0]+" " +
                                (chatPage.getPageNumber() != 1 ? chatPage.getPageNumber() - 1 : chatPage.getTotalPages())
                )
        );

        TextComponent next = new TextComponent("->");
        next.setClickEvent(
                new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/getrating " + args[0] + " " +
                                (chatPage.getPageNumber() != chatPage.getTotalPages() ? chatPage.getPageNumber() + 1 : 1)
                )
        );

        TextComponent msg = new TextComponent();

        msg.addExtra(back);
        msg.addExtra(String.format(" %d/%d ", chatPage.getPageNumber(), chatPage.getTotalPages()));
        msg.addExtra(next);

        sender.spigot().sendMessage(msg);
        return true;
    }
}
