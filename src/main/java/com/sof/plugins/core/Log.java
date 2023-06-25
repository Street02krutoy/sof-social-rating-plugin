package com.sof.plugins.core;

public class Log {
    private final String reason;
    private final int amount;
    private final String player;

    private Log(String reason, int amount, String player) {
        this.reason = reason;
        this.player = player;
        this.amount = amount;
    }

    public static Log from(String reason, int amount, String player) {
        return new Log(reason, amount, player);
    };



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
                '}';
    }
}
