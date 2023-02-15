package com.sof.plugins.core;

public class Log {
    private final String reason;
    private final int amount;
    private final String player;

    private Log(String data) {
        String[] parsed = data.split(";", 3);
        this.reason = parsed[2];
        this.player = parsed[1];
        this.amount = Integer.parseInt(parsed[0]);
    }

    public static Log from(String data) {
        return new Log(data);
    }

    public static String from(String reason, int amount, String player) {
        return amount+";"+player+";"+reason;
    };

    @Override
    public String toString() {
        return String.format("%d;%s", amount, reason);
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
}
