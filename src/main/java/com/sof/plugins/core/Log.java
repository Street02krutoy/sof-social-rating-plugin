package com.sof.plugins.core;

public class Log {
    private final String reason;
    private final int amount;
    private final String player;
    private final String moderator;

    private Log(String reason, int amount, String player, String moderator) {
        this.reason = reason;
        this.player = player;
        this.amount = amount;
        this.moderator = moderator;
    }

    public static Log from(String reason, int amount, String player, String moderator) {
        return new Log(reason, amount, player, moderator == null ? "Server" : moderator);
    };


    public String getModerator() {
        return moderator;
    }

    public int getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "Log{" +
                "reason='" + reason + '\'' +
                ", amount=" + amount +
                ", player='" + player + '\'' +
                ", moderator='" + moderator + '\'' +
                '}';
    }
}
