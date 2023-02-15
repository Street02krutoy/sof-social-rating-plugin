package com.sof.plugins.core.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.sof.plugins.core.Log;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class User {
    private String nickname;
    private int rating;
    private final Document document;
    private List<String> reasons;
    private final MongoCollection collection;

    public User (String nickname, DatabaseManager manager) {
        reasons = new ArrayList<String>();
        MongoCollection<Document> collection = manager.getDatabase().getCollection("user");
        Document user = collection.find(eq("nickname", nickname)).first();

        if(user != null) {
            this.nickname = user.getString("nickname");
            this.rating = user.getInteger("rating");
            this.reasons = (ArrayList<String>) user.get("reasons");
            this.document = user;
            this.collection = collection;
            return;
        };


        collection.insertOne(new Document("nickname", nickname).append("rating", 0).append("reasons", reasons));
        user = collection.find(eq("nickname", nickname)).first();
        System.out.println(user.toJson());

        this.nickname = user.getString("nickname");
        this.rating = user.getInteger("rating");
        this.reasons = (List<String>) user.get("reasons");
        this.document = user;
        this.collection = collection;
    }

    private void save() {
        UpdateResult result = collection.updateOne(new Document().append("nickname", nickname), Updates.combine(
                Updates.set("rating", this.rating),
                Updates.set(
                            "reasons",
                            this.reasons
                        )
        ), new UpdateOptions().upsert(true));
    }

    public void addRating(int rating, String reason, String user) {
        this.rating = this.rating + rating;
        document.put("rating", this.rating);
        this.reasons.add(Log.from(reason, rating, user));
        this.save();
    }

    public int getRating() {
        return rating;
    }

    public List<Log> getReasons() {
        List<Log> a = new ArrayList<>();
        reasons.forEach((s -> {
            a.add(Log.from(s));
        }));
        return a;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", rating=" + rating +
                '}';
    }
}