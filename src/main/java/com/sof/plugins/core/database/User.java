package com.sof.plugins.core.database;

import com.sof.plugins.core.Log;

import java.util.List;
public class User {

    private String nickname;
    private List<Log> reasons;
    private int rating;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Log> getReasons() {
        return reasons;
    }

    public void setReasons(List<Log> reasons) {
        this.reasons = reasons;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
