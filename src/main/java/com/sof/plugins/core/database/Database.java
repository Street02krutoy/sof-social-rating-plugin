package com.sof.plugins.core.database;

import com.sof.plugins.core.Log;

import java.sql.*;
import java.util.*;

public class Database {

    private static Connection conn;
    public static void init() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:./plugins/core/sof.db");

        Statement st = conn.createStatement();
        
        st.executeUpdate("CREATE TABLE IF NOT EXISTS users(" +
                "nickname VARCHAR(255) NOT NULL UNIQUE," +
                "rating INT NOT NULL"+
                ")");

        st.executeUpdate("CREATE TABLE IF NOT EXISTS logs(" +
                "nickname VARCHAR(255) NOT NULL," +
                "rating INT NOT NULL,"+
                "reason VARCHAR(255),"+
                "id INTEGER PRIMARY KEY AUTOINCREMENT"+
                ")");
    }

    private static List<Log> getLogs(String nickname) throws SQLException {

        List<Log> logs = new ArrayList<>();

        ResultSet set = conn.createStatement().executeQuery(String.format("SELECT * FROM logs WHERE nickname = '%s'", nickname));

        if(!set.isBeforeFirst()) return logs;
        while (set.next()){
            logs.add(Log.from(set.getString("reason"), set.getInt("rating"), set.getString("nickname")));
        }

        return logs;
    };


    public static User getUser(String nickname) throws SQLException {
        ResultSet set = conn.createStatement().executeQuery(String.format("SELECT * FROM users WHERE nickname = '%s'", nickname));

        if(!set.next()) {
            return createUser(nickname);
        }

        User user = new User();

        user.setNickname(set.getString("nickname"));
        user.setRating (set.getInt("rating"));
        try {
            user.setReasons(getLogs(nickname));
        }catch (SQLException e){
            user.setReasons(new ArrayList<>());
        }

        return user;
    }

    public static void addRating(String nickname, int rating, String reason) throws SQLException {

        PreparedStatement st = conn.prepareStatement("INSERT INTO logs(nickname,rating,reason) VALUES (?,?,?)");

        st.setString(1, nickname);
        st.setInt(2, rating);
        st.setString(3, reason);

        User user = getUser(nickname);

        conn.createStatement().executeUpdate(String.format("UPDATE users SET rating = %d WHERE nickname = '%s'", user.getRating()+rating, nickname));

        st.executeUpdate();

        //conn.commit();
    }

    public static void addRating(User user, int rating, String reason) throws SQLException {

        PreparedStatement st = conn.prepareStatement("INSERT INTO logs(nickname,rating,reason) VALUES (?,?,?)");

        st.setString(1, user.getNickname());
        st.setInt(2, rating);
        st.setString(3, reason);

        conn.createStatement().executeUpdate(String.format("UPDATE users SET rating = %d WHERE nickname = '%s'", user.getRating()+rating, user.getNickname()));

        st.executeUpdate();

        user.setRating(user.getRating()+rating);

        //conn.commit();
    }

    private static User createUser(String nickname) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO users(nickname,rating) VALUES (?,?)");

        User user = new User();

        user.setNickname(nickname);
        user.setRating(0);
        user.setReasons(new ArrayList<>());

        st.setString(1, user.getNickname());
        st.setInt(2, 0);
        st.executeUpdate();

        addRating(user, 100, "Начальный рейтинг");

        return user;
    }

}
